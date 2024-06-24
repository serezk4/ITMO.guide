package com.serezk4.io.deque;

import com.serezk4.configuration.RecursionConfiguration;
import com.serezk4.io.IOWorker;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Worker for reading and writing data to the deque.
 * (for command exec)
 *
 * @see IOWorker
 */
public class DequeWorker implements IOWorker<String> {
    private final Deque<String> deque = new ArrayDeque<>();
    private int recursionDepth;

    /**
     * Reads the first element from the deque.
     *
     * @return the first element from the deque
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
     *
     * @param data data to write
     */
    @Override
    public void write(String data) {
        // do nothing
    }

    /**
     * Inserts data to the deque.
     * @param data data
     */
    @Override
    public void insert(String data) {
        if (data == null) return;
        deque.addAll(Arrays.asList(data.split(System.lineSeparator())));
        recursionDepth++;
    }

    /**
     * Checks if the deque is empty.
     *
     * @return true if the deque is empty, false otherwise
     */
    @Override
    public boolean ready() {
        return deque.isEmpty();
    }

    /**
     * Clears the deque.
     */
    @Override
    public void close() {
        deque.clear();
    }
}
