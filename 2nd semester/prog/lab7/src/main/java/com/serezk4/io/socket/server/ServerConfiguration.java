package com.serezk4.io.socket.server;

/**
 * Configuration for the {@link Server}.
 * <p>
 * This record defines the essential configuration settings required for running a {@link Server}.
 * It includes the server's port number and buffer size, which are critical for managing
 * network communication and data transfer.
 * </p>
 *
 * @param port       the port on which the server will listen for incoming connections.
 *                   Must be within the valid range (1-65535).
 * @param bufferSize the size of the buffer to be used for reading and writing data.
 *                   Must be greater than 0.
 * @since 1.0
 */
public record ServerConfiguration(int port, int bufferSize) {

    /**
     * Constructs a new {@code ServerConfiguration} with the specified port and buffer size.
     *
     * @param port       the port on which the server will listen for incoming connections
     * @param bufferSize the size of the buffer to be used for data transfer
     * @throws IllegalArgumentException if the port is not in the range (1-65535)
     *                                  or if the buffer size is less than or equal to 0
     */
    public ServerConfiguration {
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be in the range 1-65535.");
        }
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0.");
        }
    }

    /**
     * Provides a string representation of the {@code ServerConfiguration}.
     * <p>
     * The string includes the port and buffer size in a readable format.
     * </p>
     *
     * @return a string representation of the {@code ServerConfiguration}
     */
    @Override
    public String toString() {
        return "ServerConfiguration {" +
                "port=" + port +
                ", bufferSize=" + bufferSize +
                '}';
    }
}
