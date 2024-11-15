package com.serezk4.io.socket.server;

import com.serezk4.chat.Router;
import com.serezk4.collection.CollectionManager;
import com.serezk4.io.console.ConsoleWorker;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A multi-threaded server for handling client connections and processing requests.
 *
 * <p>This class uses Java NIO for non-blocking socket operations, enabling scalability for multiple
 * simultaneous connections. It processes requests and responses asynchronously with dedicated
 * thread pools for reading and writing operations. Console commands allow for administrative
 * control of the server.</p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Accepts and manages multiple client connections.</li>
 *     <li>Processes requests using {@link Router}.</li>
 *     <li>Handles administrative console commands for server control.</li>
 *     <li>Graceful shutdown and resource cleanup.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * Server server = new Server(configuration, consoleWorker);
 * new Thread(server).start();
 * </pre>
 *
 * <p>Thread Safety:</p>
 * <p>This class is thread-safe, with proper synchronization for shared resources.</p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class Server implements AutoCloseable, Runnable {

    /**
     * Logger for recording server activities, errors, and debugging information.
     */
    private static final Logger log = LoggerFactory.getLogger(Server.class.getSimpleName());

    /**
     * Size of the buffer used for reading and writing data, in bytes.
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Capacity of the task queue used by the thread pools for managing pending tasks.
     */
    private static final int QUEUE_CAPACITY = 1000;

    /**
     * Configuration for the server, containing settings such as port and other properties.
     */
    private final ServerConfiguration configuration;

    /**
     * Console worker for processing administrative commands entered by the user.
     */
    private final ConsoleWorker console;

    /**
     * Server socket channel used for accepting incoming client connections.
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * Selector for monitoring I/O events such as connection acceptance, reading, and writing.
     */
    private Selector selector;

    /**
     * Thread pool for managing tasks related to reading data from clients.
     */
    private final ExecutorService readPool;

    /**
     * Thread pool for managing tasks related to writing data to clients.
     */
    private final ExecutorService writePool;

    /**
     * Map storing client-specific data for each connected client, identified by their socket channel.
     */
    private final Map<SocketChannel, ClientData> clientDataMap = new ConcurrentHashMap<>();

    /**
     * Atomic flag to indicate whether the server is currently running.
     *
     * <p>This flag is used to control the server's main loop and ensure thread-safe updates
     * when starting or stopping the server.</p>
     */
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    /**
     * Constructs a new {@code Server} instance with the specified configuration and console worker.
     *
     * <p>This constructor initializes the server with the necessary configuration settings and
     * console for administrative commands. It also creates thread pools for handling read and
     * write operations and initializes the server resources.</p>
     *
     * <p>The constructor ensures that neither the configuration nor the console worker is {@code null}.
     * If either is null, a {@link NullPointerException} is thrown.</p>
     *
     * @param configuration the {@link ServerConfiguration} containing server settings, such as the port.
     * @param console       the {@link ConsoleWorker} for handling administrative commands via the console.
     * @throws NullPointerException if {@code configuration} or {@code console} is {@code null}.
     */
    public Server(ServerConfiguration configuration, ConsoleWorker console) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.console = Objects.requireNonNull(console, "ConsoleWorker cannot be null");
        this.readPool = createThreadPool("ReadPool");
        this.writePool = createThreadPool("WritePool");
        initServer();
    }

    /**
     * Creates a thread pool for managing tasks with a specified name prefix for its threads.
     *
     * <p>This method creates a {@link ThreadPoolExecutor} configured with:</p>
     * <ul>
     *     <li>A fixed number of threads equal to the number of available processors.</li>
     *     <li>An {@link ArrayBlockingQueue} with a capacity defined by {@code QUEUE_CAPACITY} to
     *         store tasks awaiting execution.</li>
     *     <li>Daemon threads with a custom name prefix to aid in debugging.</li>
     * </ul>
     *
     * <p>The thread pool ensures that tasks are executed concurrently without blocking the main thread,
     * making it suitable for handling I/O operations like reading and writing client data.</p>
     *
     * <p>Usage:</p>
     * <pre>
     * ExecutorService pool = createThreadPool("ReadPool");
     * </pre>
     *
     * @param name the prefix for the names of threads in the thread pool.
     * @return a configured {@link ExecutorService} instance for executing tasks.
     */
    private ExecutorService createThreadPool(String name) {
        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("pool-".concat(name));
                    return thread;
                }
        );
    }

    /**
     * Initializes the server by setting up the socket channel and selector.
     *
     * <p>This method performs the necessary setup for the server to start accepting connections:
     * it opens a {@link ServerSocketChannel}, binds it to the configured port, sets it to non-blocking
     * mode, and registers it with a {@link Selector} to monitor {@link SelectionKey#OP_ACCEPT} events.</p>
     *
     * <p>If any I/O error occurs during initialization, the server logs the error and throws
     * an {@link IllegalStateException} to prevent further operations.</p>
     *
     * @throws IllegalStateException if the server fails to initialize due to an I/O error.
     */
    private void initServer() {
        try {
            log.info("Initializing server...");
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(configuration.port()));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            log.info("Server initialized successfully on port {}", configuration.port());
        } catch (IOException e) {
            log.error("Failed to initialize server", e);
            throw new IllegalStateException("Server initialization failed", e);
        }
    }

    /**
     * Starts the server's main loop, handling console commands and I/O events.
     *
     * <p>This method runs the server's main loop, which alternates between processing console
     * commands and handling selector keys for I/O events. The loop continues until the
     * {@code isRunning} flag is set to {@code false}.</p>
     *
     * <p>Any unexpected exceptions are logged, and the server shuts down gracefully in the
     * {@code finally} block.</p>
     */
    @Override
    public void run() {
        log.info("Starting server main loop...");
        try {
            while (isRunning.get()) {
                processConsoleCommands();
                processSelectorKeys();
            }
        } catch (Exception e) {
            log.error("Unexpected error in main loop", e);
        } finally {
            log.info("Shutting down server main loop...");
            close();
        }
    }

    /**
     * Processes administrative commands entered via the console.
     *
     * <p>This method checks if there is input available from the console and, if so, reads
     * a command and passes it to the {@link #handleCommand(String)} method for processing.</p>
     *
     * <p>Common commands include:</p>
     * <ul>
     *     <li>{@code exit}: Shuts down the server.</li>
     *     <li>{@code save}: Saves the current state of data managed by {@link CollectionManager}.</li>
     * </ul>
     *
     * <p>Any errors encountered during input reading are logged as warnings.</p>
     */
    private void processConsoleCommands() {
        try {
            if (console.ready()) {
                String command = console.read("> ");
                log.debug("Received console command: {}", command);
                handleCommand(command);
            }
        } catch (IOException e) {
            log.warn("Error reading console input", e);
        }
    }

    /**
     * Handles specific commands entered via the console.
     *
     * <p>This method processes commands recognized by the server, such as shutting down the
     * server or saving data. If an unknown command is entered, it logs a warning and writes
     * an error message to the console.</p>
     *
     * <p>Supported commands:</p>
     * <ul>
     *     <li>{@code exit}: Stops the server by setting {@code isRunning} to {@code false} and
     *         waking up the {@link Selector} to exit the main loop.</li>
     *     <li>{@code save}: Invokes the {@link CollectionManager#save()} method to persist data.</li>
     *     <li>Other commands: Writes an "Unknown command" message to the console.</li>
     * </ul>
     *
     * @param command the command entered by the user via the console.
     * @throws IOException if an I/O error occurs while handling the command.
     */
    private void handleCommand(String command) throws IOException {
        if (command == null) return;

        switch (command.trim().toLowerCase()) {
            case "exit":
                log.info("Received 'exit' command. Shutting down server...");
                isRunning.set(false);
                selector.wakeup();
                break;
            case "save":
                log.info("Received 'save' command. Saving data...");
                CollectionManager.getInstance().save();
                log.info("Data saved successfully.");
                break;
            default:
                log.warn("Unknown command received: {}", command);
                console.write("Unknown command\n");
        }
    }

    /**
     * Processes selector keys to handle I/O events for connected clients.
     *
     * <p>This method retrieves the set of selected keys from the {@link Selector} and iterates over
     * them. Each key represents an I/O event such as accepting a connection, reading data, or
     * writing data. The method delegates the handling of these events to the appropriate handlers
     * based on the key's state.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Writes a progress indicator to the console.</li>
     *     <li>Uses {@code selector.select()} to wait for I/O events with a timeout.</li>
     *     <li>Iterates through the selected keys and processes each one:</li>
     *     <ul>
     *         <li>If the key is acceptable, a new connection is accepted.</li>
     *         <li>If the key is readable, data is read from the client.</li>
     *         <li>If the key is writable, data is written to the client.</li>
     *     </ul>
     * </ul>
     *
     * @throws IOException if an I/O error occurs during selector operations.
     */
    private void processSelectorKeys() throws IOException {
        selector.select(100);

        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            keyIterator.remove();

            if (!key.isValid()) {
                log.warn("Invalid key encountered: {}", key);
                continue;
            }

            try {
                if (key.isAcceptable()) {
                    log.info("Acceptable key detected. Accepting connection...");
                    acceptConnection();
                } else if (key.isReadable()) {
                    log.info("Readable key detected. Processing read...");
                    processRead(key);
                } else if (key.isWritable()) {
                    log.info("Writable key detected. Processing write...");
                    processWrite(key);
                }
            } catch (IOException e) {
                log.warn("Error processing selector key: {}", key, e);
                closeConnection(key);
            }
        }
    }

    /**
     * Accepts a new client connection and registers it with the selector.
     *
     * <p>This method is invoked when a key with the {@link SelectionKey#OP_ACCEPT} operation is
     * detected. It accepts the client's connection, configures it for non-blocking mode, registers
     * it with the selector for reading operations, and initializes its {@link ClientData}.</p>
     *
     * @throws IOException if an error occurs while accepting or configuring the connection.
     */
    private void acceptConnection() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        clientDataMap.put(client, new ClientData());
        log.info("New client connected: {}", client.getRemoteAddress());
    }

    /**
     * Processes a readable key by delegating the read operation to a thread in the read pool.
     *
     * <p>This method is invoked when a key with the {@link SelectionKey#OP_READ} operation is
     * detected. It retrieves the associated {@link SocketChannel} and {@link ClientData}, then
     * submits a read task to the read thread pool.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Retrieves the client's channel and {@link ClientData} from the key.</li>
     *     <li>Submits a task to the read pool to handle the read operation.</li>
     *     <li>On successful read, processes the client's data.</li>
     *     <li>Handles any I/O errors by logging and closing the connection.</li>
     * </ul>
     *
     * @param key the {@link SelectionKey} associated with the readable client channel.
     */
    private void processRead(SelectionKey key) {
        readPool.execute(() -> {
            try {
                SocketChannel client = (SocketChannel) key.channel();
                ClientData clientData = clientDataMap.get(client);

                if (clientData != null && readFromClient(client, key, clientData)) {
                    processClientData(client, clientData, key);
                }
            } catch (IOException e) {
                log.error("Error reading from client", e);
                closeConnection(key);
            }
        });
    }

    /**
     * Reads data from the client channel into the read buffer.
     *
     * <p>This method attempts to read data from the client's {@link SocketChannel} into the
     * {@link ByteBuffer} stored in the client's {@link ClientData}. If the end of the stream
     * is reached, the connection is closed. Otherwise, it logs the number of bytes read.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Retrieves the client's read buffer.</li>
     *     <li>Reads data from the client channel into the buffer.</li>
     *     <li>If the end of the stream is detected, closes the connection.</li>
     *     <li>Logs the number of bytes read and returns {@code true} if data was successfully read.</li>
     * </ul>
     *
     * @param client     the client's {@link SocketChannel}.
     * @param key        the {@link SelectionKey} associated with the client channel.
     * @param clientData the {@link ClientData} object containing the client's read buffer.
     * @return {@code true} if data was read successfully; {@code false} otherwise.
     * @throws IOException if an error occurs while reading from the client channel.
     */
    private boolean readFromClient(SocketChannel client, SelectionKey key, ClientData clientData) throws IOException {
        ByteBuffer buffer = clientData.getReadBuffer();
        int bytesRead = client.read(buffer);

        if (bytesRead == -1) {
            log.info("Client disconnected: {}", client.getRemoteAddress());
            closeConnection(key);
            return false;
        }

        log.debug("Read {} bytes from client: {}", bytesRead, client.getRemoteAddress());
        return bytesRead > 0;
    }

    /**
     * Processes data received from the client and handles complete requests.
     *
     * <p>This method examines the client's read buffer for complete requests. If a complete request is
     * found, it extracts the data, deserializes it into a {@link Request}, and delegates it for further
     * processing. Incomplete requests are left in the buffer for future reads.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>The buffer is flipped to prepare it for reading.</li>
     *     <li>The method iterates through the buffer, checking for complete requests.</li>
     *     <li>When a complete request is detected, the corresponding data is extracted and processed.</li>
     *     <li>The buffer is compacted at the end to preserve unread data.</li>
     * </ul>
     *
     * @param client     the client's {@link SocketChannel}.
     * @param clientData the {@link ClientData} object containing the client's buffer and write queue.
     * @param key        the {@link SelectionKey} associated with the client channel.
     * @throws IOException if an I/O error occurs while reading the buffer.
     */
    private void processClientData(SocketChannel client, ClientData clientData, SelectionKey key) throws IOException {
        ByteBuffer buffer = clientData.getReadBuffer();
        buffer.flip();

        while (buffer.remaining() >= Integer.BYTES) {
            buffer.mark();
            int length = buffer.getInt();

            if (buffer.remaining() < length) {
                buffer.reset();
                break;
            }

            byte[] data = new byte[length];
            buffer.get(data);
            handleClientRequest(client, data, key);
        }

        buffer.compact();
    }

    /**
     * Handles a deserialized request from a client, processes it, and queues a response.
     *
     * <p>This method is responsible for routing the deserialized {@link Request} using the {@link Router},
     * generating an appropriate {@link Response}, and queuing it for sending back to the client.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>The request data is deserialized into a {@link Request} object.</li>
     *     <li>The request is routed through the {@link Router} to produce a response.</li>
     *     <li>The response is added to the client's write queue for sending.</li>
     *     <li>The client's selection key is updated to allow writing.</li>
     * </ul>
     *
     * <p>Error handling ensures that any failure in deserialization or processing is logged without
     * disrupting the server workflow.</p>
     *
     * @param client the client's {@link SocketChannel}.
     * @param data   the byte array containing the serialized {@link Request} object.
     * @param key    the {@link SelectionKey} associated with the client channel.
     */
    private void handleClientRequest(SocketChannel client, byte[] data, SelectionKey key) {
        try {
            Request request = SerializationUtils.deserialize(data);
            log.info("Processing request from client {}: {}", client.getRemoteAddress(), request);

            Response response = Router.getInstance().route(request);
            clientDataMap.get(client).queueResponse(response);

            log.info("Response queued for client {}: {}", client.getRemoteAddress(), response);
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (Exception e) {
            log.error("Failed to process client request", e);
        }
    }

    /**
     * Processes writable keys by sending queued responses to the client.
     *
     * <p>This method is executed in the write thread pool and ensures that all queued responses for
     * the client are sent. If all responses are successfully sent, the key's interest is updated to
     * {@code OP_READ} to resume reading from the client.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>The method retrieves the client channel and associated {@link ClientData}.</li>
     *     <li>The {@link ClientData#getWriteQueue()} is processed, sending responses to the client.</li>
     *     <li>If the write operation is complete, the key's interest is updated to allow reading.</li>
     * </ul>
     *
     * <p>Any exceptions encountered during writing are logged, and the connection is closed if necessary.</p>
     *
     * @param key the {@link SelectionKey} associated with the client channel.
     */
    private void processWrite(SelectionKey key) {
        writePool.execute(() -> {
            try {
                SocketChannel client = (SocketChannel) key.channel();
                ClientData clientData = clientDataMap.get(client);

                if (clientData != null && writeToClient(client, clientData)) {
                    key.interestOps(SelectionKey.OP_READ);
                }
            } catch (IOException e) {
                log.error("Error writing to client", e);
                closeConnection(key);
            }
        });
    }

    /**
     * Writes data from the client's write queue to the client channel.
     *
     * <p>This method retrieves the {@link ByteBuffer} objects from the client's write queue and sends
     * the data to the client. If the buffer cannot be completely written in one operation, it leaves
     * the remaining data in the buffer and returns {@code false} to indicate a partial write.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Iterates through the write queue of {@link ByteBuffer} objects.</li>
     *     <li>Attempts to write each buffer's content to the client's socket channel.</li>
     *     <li>If a buffer has remaining data, the method stops and waits for the next writable event.</li>
     *     <li>When the queue is empty, the method returns {@code true} to signal completion.</li>
     * </ul>
     *
     * <p>Partial writes ensure that large responses or slow clients do not block other server operations.</p>
     *
     * @param client     the {@link SocketChannel} representing the client connection.
     * @param clientData the {@link ClientData} object containing the write queue and buffers.
     * @return {@code true} if all data was written successfully; {@code false} for partial writes.
     * @throws IOException if an error occurs while writing to the client channel.
     */
    private boolean writeToClient(SocketChannel client, ClientData clientData) throws IOException {
        Queue<ByteBuffer> queue = clientData.getWriteQueue();

        while (!queue.isEmpty()) {
            ByteBuffer buffer = queue.peek();
            client.write(buffer);

            if (buffer.hasRemaining()) {
                log.debug("Partial write to client: {}", client.getRemoteAddress());
                return false;
            }
            queue.poll();
        }

        log.debug("Write completed for client: {}", client.getRemoteAddress());
        return true;
    }

    /**
     * Closes a client connection and removes associated resources.
     *
     * <p>This method is called when a client disconnects or when an error occurs during I/O
     * operations. It ensures the connection is closed gracefully, the client's data is removed from
     * the {@code clientDataMap}, and the {@link SelectionKey} is canceled to prevent further events.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Retrieves the client's {@link SocketChannel} from the selection key.</li>
     *     <li>Removes the client from the {@code clientDataMap}.</li>
     *     <li>Cancels the client's {@link SelectionKey}.</li>
     *     <li>Closes the client's socket channel.</li>
     * </ul>
     *
     * <p>Errors during the close operation are logged but do not interrupt the server's workflow.</p>
     *
     * @param key the {@link SelectionKey} associated with the client connection.
     */
    private void closeConnection(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();
            clientDataMap.remove(client);
            key.cancel();
            client.close();
            log.info("Closed connection for client: {}", client.getRemoteAddress());
        } catch (IOException e) {
            log.error("Error closing connection: {}", e.getMessage());
        }
    }

    /**
     * Closes the server and releases all associated resources.
     *
     * <p>This method performs a graceful shutdown of the server, ensuring all active connections are
     * closed, thread pools are terminated, and server resources like the {@link Selector} and
     * {@link ServerSocketChannel} are released.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Sets the {@code isRunning} flag to {@code false} to terminate the server loop.</li>
     *     <li>Closes the {@link ServerSocketChannel} and {@link Selector}.</li>
     *     <li>Iterates through all connected clients and safely closes their channels.</li>
     *     <li>Shuts down the read and write thread pools.</li>
     * </ul>
     *
     * <p>Errors during the shutdown process are logged but do not halt the shutdown sequence.</p>
     */
    @Override
    public void close() {
        log.info("Shutting down server...");
        isRunning.set(false);

        try {
            if (serverSocketChannel != null) serverSocketChannel.close();
            if (selector != null) selector.close();

            clientDataMap.keySet().forEach(this::safeCloseClient);

            readPool.shutdown();
            writePool.shutdown();
            log.info("Server shut down successfully.");
        } catch (Exception e) {
            log.error("Error during server shutdown", e);
        }
    }

    /**
     * Safely closes a single client connection.
     *
     * <p>This method attempts to close the client's {@link SocketChannel} and logs any errors that
     * occur during the operation. It ensures that resources are released without disrupting other
     * shutdown processes.</p>
     *
     * @param client the {@link SocketChannel} representing the client connection.
     */
    private void safeCloseClient(SocketChannel client) {
        try {
            client.close();
        } catch (IOException e) {
            log.warn("Error closing client connection", e);
        }
    }

    /**
     * A container for client-specific data used during communication with the server.
     *
     * <p>This class encapsulates the client's read buffer and a write queue for storing outgoing
     * responses. It provides methods for accessing these buffers and for queuing responses
     * to be sent to the client.</p>
     *
     * <p>Thread Safety:</p>
     * <p>The {@link ConcurrentLinkedQueue} ensures that the write queue is thread-safe and can be
     * accessed by multiple threads concurrently without additional synchronization.</p>
     */
    private static class ClientData {

        /**
         * Buffer for reading data from the client.
         */
        private final ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        /**
         * Queue for storing responses to be written to the client.
         */
        private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();

        /**
         * Retrieves the read buffer for this client.
         *
         * <p>The read buffer is used to temporarily store incoming data from the client
         * before it is processed. It is allocated with a size defined by {@code BUFFER_SIZE}.</p>
         *
         * @return the {@link ByteBuffer} used for reading data.
         */
        public ByteBuffer getReadBuffer() {
            return readBuffer;
        }

        /**
         * Retrieves the write queue for this client.
         *
         * <p>The write queue holds {@link ByteBuffer} objects representing responses
         * to be sent to the client. Responses are added to this queue and processed
         * sequentially during write operations.</p>
         *
         * @return the {@link Queue} of {@link ByteBuffer} objects to be written.
         */
        public Queue<ByteBuffer> getWriteQueue() {
            return writeQueue;
        }

        /**
         * Queues a response to be sent to the client.
         *
         * <p>This method serializes the given {@link Response} object, wraps the serialized
         * data in a {@link ByteBuffer}, and adds it to the client's write queue. The buffer
         * includes the length of the serialized data as a 4-byte integer at the beginning.</p>
         *
         * <p>Workflow:</p>
         * <ul>
         *     <li>The {@link Response} object is serialized to a byte array.</li>
         *     <li>A {@link ByteBuffer} is created with enough capacity to hold the
         *     serialized data and its length.</li>
         *     <li>The length of the data is added to the buffer, followed by the data itself.</li>
         *     <li>The buffer is flipped to prepare it for writing, and it is added to the write queue.</li>
         * </ul>
         *
         * @param response the {@link Response} object to be queued for the client.
         */
        public void queueResponse(Response response) {
            byte[] data = SerializationUtils.serialize(response);
            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();
            writeQueue.add(buffer);
        }
    }
}
