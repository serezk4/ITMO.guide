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
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Server class that handles client connections and processes requests.
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class Server implements AutoCloseable, Runnable {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private static final int BUFFER_SIZE = 8192;
    private static final int QUEUE_CAPACITY = 1000;

    private final ServerConfiguration configuration;
    private final ConsoleWorker console;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private final ExecutorService readPool;
    private final ExecutorService writePool;

    private final Map<SocketChannel, ClientData> clientDataMap = new ConcurrentHashMap<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public Server(ServerConfiguration configuration, ConsoleWorker console) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.console = Objects.requireNonNull(console, "ConsoleWorker cannot be null");
        this.readPool = createThreadPool("ReadPool");
        this.writePool = createThreadPool("WritePool");
        initServer();
    }

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

    private void processSelectorKeys() throws IOException {
        console.write(".");
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

    private void acceptConnection() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        clientDataMap.put(client, new ClientData());
        log.info("New client connected: {}", client.getRemoteAddress());
    }

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

    private void safeCloseClient(SocketChannel client) {
        try {
            client.close();
        } catch (IOException e) {
            log.warn("Error closing client connection", e);
        }
    }

    /**
     * Client data container.
     */
    private static class ClientData {
        private final ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();

        public ByteBuffer getReadBuffer() {
            return readBuffer;
        }

        public Queue<ByteBuffer> getWriteQueue() {
            return writeQueue;
        }

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