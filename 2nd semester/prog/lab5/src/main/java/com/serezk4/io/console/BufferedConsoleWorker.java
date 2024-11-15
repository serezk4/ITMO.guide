package com.serezk4.io.console;

import java.io.*;

/**
 * Buffered implementation of the ConsoleWorker interface.
 *
 * @see ConsoleWorker
 * <p>
 * for more information >>
 * @see BufferedReader
 * @see BufferedWriter
 */

public class BufferedConsoleWorker implements ConsoleWorker {
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    /**
     * Reads data from the console.
     *
     * @return data in String format
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
     * Writes data to the console.
     *
     * @param data data in String format
     */
    @Override
    public void write(String data) {
        try {
            consoleWriter.append(data).flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Checks if the console is ready to read data.
     *
     * @return true if the console is ready, false otherwise
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
     * Closes the console reader and writer.
     *
     * @throws Exception if an I/O error occurs
     */
    @Override
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
