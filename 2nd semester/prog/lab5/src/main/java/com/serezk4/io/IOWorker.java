package com.serezk4.io;

/**
 * Interface for reading and writing data.
 *
 * @param <T> transfer data type
 */

public interface IOWorker<T> extends AutoCloseable {
    /**
     * Reads data.
     * @return data
     */
    T read();

    /**
     * Writes data.
     * @param data data
     */
    void write(T data);

    /**
     * Checks if the worker is ready to read/write data.
     * @return true if the worker is ready, false otherwise
     */
    boolean ready();
}
