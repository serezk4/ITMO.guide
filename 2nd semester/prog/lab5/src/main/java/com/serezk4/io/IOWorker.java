package com.serezk4.io;

/**
 * Interface for reading and writing data.
 * <p>
 * The {@code IOWorker} interface provides methods for handling input and output operations,
 * including reading, writing, and checking readiness. It also supports optional prompts
 * and data insertion for extended functionality.
 * </p>
 *
 * @param <T> the type of data handled by the worker
 * @see AutoCloseable
 * @since 1.0
 */
public interface IOWorker<T> extends AutoCloseable {

    /**
     * Reads data.
     * <p>
     * This method retrieves the next available piece of data. The exact behavior depends
     * on the implementation.
     * </p>
     *
     * @return the data read, or {@code null} if no data is available
     */
    T read();

    /**
     * Reads data with a prompt.
     * <p>
     * Displays the given prompt before reading data, allowing for interactive input.
     * </p>
     *
     * @param prompt the prompt to display before reading
     * @return the data read
     */
    default T read(T prompt) {
        write(prompt);
        return read();
    }

    /**
     * Writes data.
     * <p>
     * Outputs the specified data using the worker's implementation.
     * </p>
     *
     * @param data the data to write
     */
    void write(T data);

    /**
     * Inserts data into the worker.
     * <p>
     * This method provides additional data to the worker for future operations.
     * The behavior depends on the implementation and may include buffering or
     * queueing the data.
     * </p>
     *
     * @param data the data to insert
     */
    default void insert(T data) {
    }

    /**
     * Checks if the worker is ready for I/O operations.
     * <p>
     * Returns {@code true} if the worker can perform read or write operations
     * without blocking or errors.
     * </p>
     *
     * @return {@code true} if the worker is ready, {@code false} otherwise
     */
    boolean ready();
}
