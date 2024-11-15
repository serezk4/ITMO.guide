package com.serezk4.io.deque;

import com.serezk4.configuration.RecursionConfiguration;
import com.serezk4.io.IOWorker;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Worker for reading and writing data to a deque.
 * <p>
 * This class implements the {@link IOWorker} interface and uses a {@link Deque}
 * to manage data, primarily for handling command execution. It ensures that the
 * recursion depth does not exceed the configured maximum limit.
 * </p>
 *
 * @see IOWorker
 * @see RecursionConfiguration
 * @since 1.0
 */
public final class DequeWorker implements IOWorker<String> {

    private final Deque<String> deque = new ArrayDeque<>();
    private int recursionDepth;

    /**
     * Reads and removes the first element from the deque.
     * <p>
     * If the recursion depth exceeds {@link RecursionConfiguration#MAX_RECURSION_DEPTH},
     * the deque is cleared, and recursion depth is reset to prevent stack overflow.
     * </p>
     *
     * @return the first element from the deque, or {@code null} if the deque is empty
     */
    @Override
    public String read() {
        if (recursionDepth > RecursionConfiguration.MAX_RECURSION_DEPTH) {
            close();
            recursionDepth = 0;
            return null;
        }

        return deque.pollFirst();
    }

    /**
     * Writes data to the deque.
     * <p>
     * This method does nothing, as the {@link DequeWorker} is designed to
     * insert data using the {@link #insert(String)} method instead.
     * </p>
     *
     * @param data the data to write
     */
    @Override
    public void write(final String data) {
    }

    /**
     * Inserts data into the deque.
     * <p>
     * The input data is split by line separators and added to the deque.
     * Increments the recursion depth with each insertion.
     * </p>
     *
     * @param data the data to insert
     */
    @Override
    public void insert(final String data) {
        if (data == null) return;

        deque.addAll(Arrays.asList(data.split(System.lineSeparator())));
        recursionDepth++;
    }

    /**
     * Checks if the deque is empty.
     *
     * @return {@code true} if the deque is empty, {@code false} otherwise
     */
    @Override
    public boolean ready() {
        return deque.isEmpty();
    }

    /**
     * Clears all data from the deque.
     * <p>
     * Resets the deque to an empty state, ensuring no leftover data remains.
     * </p>
     */
    @Override
    public void close() {
        deque.clear();
    }
}
