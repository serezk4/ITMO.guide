package com.serezk4.io.socket.client;

/**
 * Configuration for the {@link Client}.
 * <p>
 * This record defines the essential settings required for the client to connect to the server.
 * It includes the server host, port, and buffer size for data transfer.
 * </p>
 *
 * @param host       the server hostname or IP address (cannot be null or blank)
 * @param port       the server port number (must be in the range 1-65535)
 * @param bufferSize the buffer size for data transfer (must be greater than 0)
 * @since 1.0
 */
public record ClientConfiguration(String host, int port, int bufferSize) {

    /**
     * Constructs a {@code ClientConfiguration} with the specified parameters.
     *
     * @param host       the server hostname or IP address
     * @param port       the server port number
     * @param bufferSize the buffer size for data transfer
     * @throws IllegalArgumentException if {@code host} is null or blank, {@code port} is not in the range 1-65535,
     *                                  or {@code bufferSize} is less than or equal to 0
     */
    public ClientConfiguration {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Host must not be null or blank.");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be in the range 1-65535.");
        }
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0.");
        }
    }

    /**
     * Provides a string representation of the {@code ClientConfiguration}.
     * <p>
     * The representation includes the host, port, and buffer size in a readable format.
     * </p>
     *
     * @return a string representation of the configuration
     */
    @Override
    public String toString() {
        return "ClientConfiguration {" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", bufferSize=" + bufferSize +
                '}';
    }
}
