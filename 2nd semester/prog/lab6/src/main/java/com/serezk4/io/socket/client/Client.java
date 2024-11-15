package com.serezk4.io.socket.client;

import com.serezk4.chat.Router;
import com.serezk4.collection.model.Person;
import com.serezk4.io.IOWorker;
import com.serezk4.io.console.ConsoleWorker;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.serezk4.collection.util.InputUtil.get;

/**
 * A client implementation for interacting with the {@link com.serezk4.io.socket.server.Server}.
 * <p>
 * This class manages the following:
 * <ul>
 *     <li>Establishing and maintaining a connection with the server.</li>
 *     <li>Sending requests and receiving responses from the server.</li>
 *     <li>Executing commands and handling scripts locally.</li>
 * </ul>
 *
 * @see ClientConfiguration
 * @see Request
 * @see Response
 * @since 1.0
 */
public final class Client implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Client.class.getSimpleName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final ClientConfiguration configuration;
    private SocketChannel clientChannel;
    private final ConsoleWorker console;
    private final IOWorker<String> script;

    /**
     * Constructs a {@code Client} instance with the specified configuration and I/O workers.
     *
     * @param configuration the client configuration
     * @param console       the console worker for user input
     * @param script        the script worker for executing command scripts
     */
    public Client(ClientConfiguration configuration, ConsoleWorker console, IOWorker<String> script) {
        this.configuration = configuration;
        this.console = console;
        this.script = script;
    }

    /**
     * Initializes the client by attempting to connect to the server.
     * <p>
     * Retries the connection up to a maximum of 5 attempts with a 2-second delay between each attempt.
     * </p>
     *
     * @throws IOException if the connection cannot be established after retries
     */
    private void init() throws IOException {
        final int maxRetries = 5;
        int retryCount = 0;
        final int retryDelay = 2000;

        SocketAddress serverAddress = new InetSocketAddress(configuration.host(), configuration.port());

        while (retryCount < maxRetries) {
            try {
                clientChannel = SocketChannel.open();
                clientChannel.connect(serverAddress);
                clientChannel.configureBlocking(false);

                log.info("Waiting to establish connection...");
                while (!clientChannel.finishConnect()) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }

                log.info("Connected to server at {}:{}", configuration.host(), configuration.port());
                return;
            } catch (Exception e) {
                retryCount++;
                log.warn("Failed to connect to server (attempt {}/{})", retryCount, maxRetries);
                if (retryCount >= maxRetries) {
                    throw new IOException("Unable to connect to server after " + maxRetries + " attempts", e);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Connection attempts interrupted", ie);
                }
            }
        }
    }

    /**
     * Starts the client by connecting to the server and processing user commands.
     */
    @Override
    public void run() {
        try {
            init();
            console.writeln("Welcome to the client application!");

            String line;
            while ((line = console.read("")) != null) {
                handle(line);
                while (!script.ready()) {
                    String scriptLine = script.read();
                    handle(scriptLine);
                }
            }
        } catch (IOException e) {
            log.error("I/O error occurred", e);
            console.writeln("Connection lost. Attempting to reconnect...");
            try {
                init();
            } catch (IOException ex) {
                console.writeln("Reconnection failed. Exiting...");
                System.exit(1);
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            console.writeln("Error: " + e.getMessage());
        } finally {
            closeClientChannel();
        }
    }

    /**
     * Handles a user command or script line.
     *
     * @param line the input command or script line
     * @throws IOException if an error occurs during communication
     */
    private void handle(String line) throws IOException {
        if (line == null || line.isBlank()) return;
        if (line.equalsIgnoreCase("exit")) System.exit(0);

        Request request = parse(line);
        if (request == null) return;

        sendRequest(request);
        Response response = receiveResponse();
        if (response == null) return;

        if (response.message().contains("Authorization failed.")) {
            console.writeln("Authorization failed. Please check your credentials.");
            return;
        }

        print(response);
    }

    /**
     * Sends a request to the server.
     *
     * @param request the request to send
     * @throws IOException if an I/O error occurs
     */
    private void sendRequest(Request request) throws IOException {
        byte[] data = SerializationUtils.serialize(request);
        int bufferSize = configuration.bufferSize();
        ByteBuffer buffer = ByteBuffer.allocate(4 + Math.min(data.length, bufferSize));

        buffer.putInt(data.length);
        buffer.flip();
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }

        int offset = 0;
        while (offset < data.length) {
            int chunkSize = Math.min(bufferSize, data.length - offset);
            buffer = ByteBuffer.allocate(chunkSize);
            buffer.put(data, offset, chunkSize);
            buffer.flip();
            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
            offset += chunkSize;
        }
    }

    /**
     * Receives a response from the server.
     *
     * @return the received {@link Response}
     * @throws IOException if an I/O error occurs
     */
    private Response receiveResponse() throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        while (lengthBuffer.hasRemaining()) {
            try {
                int bytesRead = clientChannel.read(lengthBuffer);
                if (bytesRead == -1) {
                    throw new IOException("Server has closed the connection");
                }
            } catch (Exception ex) {
                log.error("Connection lost, trying to reconnect....");
                init();
                return new Response("connection lost");
            }
        }
        lengthBuffer.flip();
        int messageLength = lengthBuffer.getInt();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int bufferSize = configuration.bufferSize();
        int remaining = messageLength;

        while (remaining > 0) {
            ByteBuffer dataBuffer = ByteBuffer.allocate(Math.min(bufferSize, remaining));
            int bytesRead = clientChannel.read(dataBuffer);
            if (bytesRead == -1) {
                throw new IOException("Server has closed the connection");
            }
            dataBuffer.flip();
            byteStream.write(dataBuffer.array(), 0, bytesRead);
            remaining -= bytesRead;
        }

        byte[] data = byteStream.toByteArray();
        return SerializationUtils.deserialize(data);
    }

    /**
     * Prints the server response to the console.
     *
     * @param response the response to print
     */
    private void print(Response response) {
        if (response.message() != null && !response.message().isBlank()) {
            console.writeln(response.message());
        }
        if (response.persons() != null && !response.persons().isEmpty()) {
            console.writeln("OWNER\tID\tNAME\tCOORDINATES\tCREATION_DATE\tHEIGHT\tWEIGHT\tHAIR_COLOR\tNATIONALITY\tLOCATION");
            response.persons().forEach(person -> console.writeln(person.toString()));
        }
        if (response.script() != null && !response.script().isEmpty()) {
            script.insert(response.script());
        }
    }

    /**
     * Parses the input command line into a {@link Request}.
     *
     * @param line the input command line
     * @return the constructed {@link Request}, or {@code null} if parsing fails
     */
    private Request parse(String line) {
        final String[] parts = line.trim().split(" ", 2);

        final String command = parts[0];
        final List<String> arguments = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        final List<Person> persons = new LinkedList<>();

        int elementsRequired = getElementsRequiredFor(command);

        while (elementsRequired-- > 0) {
            try {
                persons.add(get(script.ready() ? console : script));
            } catch (InterruptedException ex) {
                console.writeln("Command interrupted: " + ex.getMessage());
                return null;
            }
        }

        return new Request(command, arguments, persons);
    }

    /**
     * Determines the number of {@link Person} objects required for the given command.
     *
     * @param command the command name
     * @return the number of required {@link Person} objects
     */
    private int getElementsRequiredFor(String command) {
        return Router.getInstance().getElementsRequiredFor(command);
    }

    /**
     * Safely closes the client channel.
     */
    private void closeClientChannel() {
        try {
            if (clientChannel != null && clientChannel.isOpen()) {
                clientChannel.close();
            }
        } catch (IOException e) {
            log.error("Failed to close the client channel", e);
        }
    }
}
