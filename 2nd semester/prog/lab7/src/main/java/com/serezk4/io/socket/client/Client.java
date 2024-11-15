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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.serezk4.collection.util.InputUtil.get;

/**
 * A client application that connects to a server, sends requests, and processes responses.
 *
 * <p>The {@code Client} class implements the {@link Runnable} interface, allowing it to run in a
 * separate thread. It handles establishing a connection to the server, sending requests based on
 * user input or scripts, and processing server responses. The client also manages reconnection
 * and cleanup in case of disconnections or errors.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Establishes and maintains a connection with the server using {@link SocketChannel}.</li>
 *     <li>Processes user input via a {@link ConsoleWorker} or scripted input via an {@link IOWorker}.</li>
 *     <li>Sends serialized {@link Request} objects to the server and receives serialized
 *         {@link Response} objects.</li>
 *     <li>Handles automatic reconnection attempts in case of connection loss.</li>
 *     <li>Gracefully shuts down the client by closing the channel and releasing resources.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * ClientConfiguration configuration = new ClientConfiguration("localhost", 8080);
 * ConsoleWorker console = new ConsoleWorker();
 * IOWorker<String> scriptWorker = new IOWorker<>();
 *
 * Client client = new Client(configuration, console, scriptWorker);
 * new Thread(client).start();
 * </pre>
 *
 * <p>Thread Safety:</p>
 * <p>This class is not thread-safe. It assumes a single-threaded context for managing its state
 * and interacting with the server.</p>
 *
 * @author serezk4
 * @version 1.0
 * @see Runnable
 * @see ConsoleWorker
 * @see IOWorker
 * @see Request
 * @see Response
 * @since 1.0
 */
public final class Client implements Runnable {

    /**
     * Logger for recording client activities, errors, and debugging information.
     */
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    /**
     * Configuration for the client, containing server host, port, and other properties.
     */
    private final ClientConfiguration configuration;

    /**
     * Socket channel for establishing and managing the connection to the server.
     */
    private SocketChannel clientChannel;

    /**
     * Console worker for handling user input and output.
     */
    private final ConsoleWorker console;

    /**
     * I/O worker for executing command scripts.
     */
    private final IOWorker<String> script;

    /**
     * Constructs a {@code Client} instance with the specified configuration and I/O workers.
     *
     * <p>This constructor initializes the client with its required dependencies: configuration for
     * server connection details, a console worker for handling user interactions, and a script worker
     * for executing predefined command scripts.</p>
     *
     * <p>All parameters are mandatory, and {@code null} values are not allowed.</p>
     *
     * @param configuration the {@link ClientConfiguration} containing server connection details.
     * @param console       the {@link ConsoleWorker} for handling user input and output.
     * @param script        the {@link IOWorker} for executing command scripts.
     * @throws NullPointerException if any of the parameters are {@code null}.
     */
    public Client(ClientConfiguration configuration, ConsoleWorker console, IOWorker<String> script) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.console = Objects.requireNonNull(console, "ConsoleWorker cannot be null");
        this.script = Objects.requireNonNull(script, "Script IOWorker cannot be null");
    }

    /**
     * Initializes the client by attempting to connect to the server.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Retrieves the server address from the configuration.</li>
     *     <li>Attempts to establish a connection with the server using a {@link SocketChannel}.</li>
     *     <li>Retries the connection up to a maximum of 5 attempts with a 2-second delay between attempts
     *         if the initial connection fails.</li>
     *     <li>Logs connection successes and failures, providing detailed information about each attempt.</li>
     * </ul>
     *
     * <p>If the connection cannot be established after the maximum number of retries, an
     * {@link IOException} is thrown to indicate the failure.</p>
     *
     * @throws IOException if the connection cannot be established after the maximum number of retries.
     */
    private void init() throws IOException {
        final int maxRetries = 5;
        final int retryDelay = 2000;

        SocketAddress serverAddress = new InetSocketAddress(configuration.host(), configuration.port());
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                TimeUnit.MILLISECONDS.sleep(retryDelay);

                clientChannel = SocketChannel.open();
                clientChannel.configureBlocking(true);
                clientChannel.connect(serverAddress);

                while (!clientChannel.finishConnect()) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }

                log.info("Connected to server at {}:{}", configuration.host(), configuration.port());
                return;
            } catch (Exception e) {
                retryCount++;
                log.warn("Failed to connect to server (attempt {}/{}): {}", retryCount, maxRetries, e.getMessage());

                if (retryCount >= maxRetries) {
                    throw new IOException("Unable to connect to server after " + maxRetries + " attempts", e);
                }
            }
        }
    }

    /**
     * Starts the client by connecting to the server and processing user commands.
     *
     * <p>This method initializes the client, connects to the server, and enters a loop to process
     * commands entered by the user or read from a script. The loop continues until the user
     * terminates the session by entering the {@code exit} command.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Initializes the client by connecting to the server using the {@link #init()} method.</li>
     *     <li>Displays a welcome message to the user via the console.</li>
     *     <li>Continuously reads user input from the console and processes it using {@link #handleInput(String)}.</li>
     *     <li>Processes script commands if a script is being executed, ensuring each line is handled.</li>
     *     <li>Handles any I/O or unexpected errors by logging and attempting to reconnect.</li>
     *     <li>Performs cleanup by closing the client channel when the session ends.</li>
     * </ul>
     *
     * <p>Error Handling:</p>
     * <ul>
     *     <li>Logs and attempts to reconnect on I/O errors.</li>
     *     <li>Logs unexpected errors and terminates gracefully.</li>
     * </ul>
     */
    @Override
    public void run() {
        try {
            init();
            console.writeln("Welcome to the client application!");

            String line;
            while ((line = console.read("")) != null) {
                handleInput(line);
                while (!script.ready()) handleInput(script.read());
            }
        } catch (IOException e) {
            log.error("I/O error occurred: {}", e.getMessage());
            reconnect();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
        } finally {
            closeClientChannel();
        }
    }

    /**
     * Handles input commands or script lines by processing and sending them to the server.
     *
     * <p>This method parses the input into a {@link Request}, sends it to the server, and
     * processes the {@link Response} received. If the input is the {@code exit} command, it
     * terminates the client session.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Ignores blank or null input lines.</li>
     *     <li>Handles the {@code exit} command by closing the client channel and exiting the application.</li>
     *     <li>Parses the input into a {@link Request} object using {@link #parseRequest(String)}.</li>
     *     <li>Sends the request to the server using {@link #sendRequest(Request)}.</li>
     *     <li>Receives and processes the server's response using {@link #receiveResponse()} and
     *         {@link #handleResponse(Response)}.</li>
     * </ul>
     *
     * @param input the input command or script line to process.
     * @throws IOException if an error occurs during communication with the server.
     */
    private void handleInput(String input) throws IOException {
        if (input == null || input.isBlank()) return;

        if ("exit".equalsIgnoreCase(input)) {
            closeClientChannel();
            System.exit(0);
        }

        Request request = parseRequest(input);
        if (request == null) return;

        sendRequest(request);
        Response response = receiveResponse();
        handleResponse(response);
    }

    /**
     * Sends a serialized request to the server.
     *
     * <p>This method serializes the {@link Request} object into a byte array, packages it into a
     * {@link ByteBuffer} with its length as a 4-byte integer prefix, and writes the buffer to the
     * server's channel.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Serializes the {@link Request} into a byte array.</li>
     *     <li>Creates a {@link ByteBuffer} to hold the length of the data and the serialized data.</li>
     *     <li>Writes the buffer to the server's {@link SocketChannel} until all bytes are sent.</li>
     * </ul>
     *
     * @param request the {@link Request} object to be sent to the server.
     * @throws IOException if an I/O error occurs while writing to the server channel.
     */
    private void sendRequest(Request request) throws IOException {
        byte[] data = SerializationUtils.serialize(request);
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);

        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();

        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }

    /**
     * Receives a serialized response from the server and deserializes it into a {@link Response} object.
     *
     * <p>This method reads the response length as a 4-byte integer, then reads the response data
     * from the server's {@link SocketChannel}. It deserializes the data into a {@link Response}
     * object using {@link SerializationUtils}.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Allocates a buffer to read the 4-byte length of the incoming response.</li>
     *     <li>Reads the length and allocates a buffer for the response data.</li>
     *     <li>Reads the response data into the buffer and deserializes it into a {@link Response} object.</li>
     * </ul>
     *
     * @return the deserialized {@link Response} object received from the server.
     * @throws IOException if an I/O error occurs while reading from the server channel.
     */
    private Response receiveResponse() throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        readFromChannel(lengthBuffer);
        lengthBuffer.flip();

        int messageLength = lengthBuffer.getInt();
        ByteBuffer dataBuffer = ByteBuffer.allocate(messageLength);

        readFromChannel(dataBuffer);
        dataBuffer.flip();

        return SerializationUtils.deserialize(dataBuffer.array());
    }

    /**
     * Reads data from the server's channel into the specified buffer.
     *
     * <p>This method ensures that the buffer is fully filled by reading from the
     * {@link SocketChannel} until all remaining bytes are consumed. If the server closes
     * the connection, an {@link IOException} is thrown.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Continuously reads data from the {@link SocketChannel} until the buffer is filled.</li>
     *     <li>If the server closes the connection during reading, throws an exception.</li>
     * </ul>
     *
     * @param buffer the {@link ByteBuffer} to read data into.
     * @throws IOException if an I/O error occurs or the server closes the connection.
     */
    private void readFromChannel(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                throw new IOException("Server has closed the connection");
            }
        }
    }

    /**
     * Handles the received server response by displaying messages, listing persons, or executing scripts.
     *
     * <p>This method processes the {@link Response} object from the server by:</p>
     * <ul>
     *     <li>Writing the response message to the console if it is not blank.</li>
     *     <li>Listing the persons from the response by printing their {@code toString()} representation
     *         to the console.</li>
     *     <li>Inserting scripts from the response into the {@link IOWorker} for execution.</li>
     * </ul>
     *
     * @param response the {@link Response} object received from the server.
     */
    private void handleResponse(Response response) {
        if (response.message() != null && !response.message().isBlank()) {
            console.writeln(response.message());
        }

        if (response.persons() != null && !response.persons().isEmpty()) {
            response.persons().forEach(person -> console.writeln(person.toString()));
        }

        if (response.script() != null && !response.script().isEmpty()) {
            script.insert(response.script());
        }
    }

    /**
     * Parses the input command line into a {@link Request} object.
     *
     * <p>This method splits the input string into a command and its arguments. It also determines the
     * number of {@link Person} objects required for the command and retrieves them either from the
     * console or the {@link IOWorker}. If the parsing or input retrieval is interrupted, the method
     * logs a warning and returns {@code null}.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Splits the input string into the command and arguments.</li>
     *     <li>Determines the required number of {@link Person} objects for the command.</li>
     *     <li>Attempts to retrieve the required {@link Person} objects from input sources.</li>
     *     <li>Constructs a {@link Request} object with the command, arguments, and persons.</li>
     * </ul>
     *
     * @param input the input command line entered by the user or read from a script.
     * @return the constructed {@link Request}, or {@code null} if parsing fails or input retrieval is interrupted.
     */
    private Request parseRequest(String input) {
        String[] parts = input.trim().split(" ", 2);
        String command = parts[0];
        List<String> arguments = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        List<Person> persons = new LinkedList<>();

        int elementsRequired = Router.getInstance().getElementsRequiredFor(command);
        while (elementsRequired-- > 0) {
            try {
                persons.add(get(script.ready() ? console : script));
            } catch (InterruptedException e) {
                log.warn("Input interrupted: {}", e.getMessage());
                return null;
            }
        }

        return new Request(command, arguments, persons);
    }

    /**
     * Reconnects the client to the server in case of disconnection.
     *
     * <p>This method attempts to reconnect the client by reinitializing the connection using
     * the {@link #init()} method. If reconnection fails, the application exits with an error
     * message.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Writes a message to the console indicating the reconnection attempt.</li>
     *     <li>Attempts to reinitialize the client connection using {@link #init()}.</li>
     *     <li>If reconnection fails, writes an error message to the console and exits the application.</li>
     * </ul>
     */
    private void reconnect() {
        console.writeln("Connection lost. Attempting to reconnect...");
        try {
            init();
        } catch (IOException e) {
            console.writeln("Reconnection failed. Exiting...");
            System.exit(1);
        }
    }

    /**
     * Safely closes the client channel.
     *
     * <p>This method checks if the client channel is open and attempts to close it. Any
     * exceptions encountered during the closing operation are logged as errors.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Checks if the {@link SocketChannel} is non-null and open.</li>
     *     <li>Closes the channel if it is open.</li>
     *     <li>Logs any errors encountered during the closing operation.</li>
     * </ul>
     */
    private void closeClientChannel() {
        try {
            if (clientChannel != null && clientChannel.isOpen()) {
                clientChannel.close();
            }
        } catch (IOException e) {
            log.error("Failed to close the client channel: {}", e.getMessage(), e);
        }
    }
}