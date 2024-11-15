package com.serezk4.io.console;

import java.io.*;

/**
 * Buffered implementation of the {@link ConsoleWorker} interface.
 * <p>
 * This class provides buffered input and output functionality for the console using
 * {@link BufferedReader} and {@link BufferedWriter}. It supports reading from and
 * writing to the console with efficient buffering.
 * </p>
 *
 * @see ConsoleWorker
 * @see BufferedReader
 * @see BufferedWriter
 * @since 1.0
 */
public final class BufferedConsoleWorker implements ConsoleWorker {

    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    /**
     * Reads a line of input from the console.
     * <p>
     * This method blocks until a line of text is entered or an error occurs.
     * </p>
     *
     * @return the input string entered by the user, or {@code null} if an I/O error occurs
     */
    @Override
    public String read() {
        try {
            return consoleReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Writes a string to the console.
     * <p>
     * The string is appended to the console output and flushed immediately.
     * </p>
     *
     * @param data the string to write to the console
     */
    @Override
    public void write(final String data) {
        try {
            consoleWriter.append(data).flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Checks if the console input stream is ready for reading.
     * <p>
     * This method returns {@code true} if there is data available in the console
     * input buffer.
     * </p>
     *
     * @return {@code true} if the console is ready to read, {@code false} otherwise
     */
    @Override
    public boolean ready() {
        try {
            return consoleReader.ready();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Closes the console reader and writer streams.
     * <p>
     * Once closed, the {@link BufferedConsoleWorker} cannot perform further
     * read or write operations.
     * </p>
     *
     * @throws Exception if an error occurs during closing
     */
    @Override
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
