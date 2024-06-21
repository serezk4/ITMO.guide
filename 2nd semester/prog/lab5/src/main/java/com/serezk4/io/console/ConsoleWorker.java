package com.serezk4.io.console;

import com.serezk4.io.IOWorker;

/**
 * Interface for reading and writing data from/to the console.
 */

public interface ConsoleWorker extends IOWorker<String> {
    /**
     * Writes data to the console.
     * @param data data
     */
    default void writeln(String data) {
        write(data + System.lineSeparator());
    }

    /**
     * Writes data to the console.
     * @param lines data
     */
    default void writeln(String... lines) {
        write(String.join(System.lineSeparator(), lines) + System.lineSeparator());
    }

    @Override
    default String read(String prompt) {
        return IOWorker.super.read(prompt + " ❯ ");
    }

    /**
     * Writes data to the console.
     * @param format format string
     * @param args arguments
     */
    default void writef(String format, Object... args) {
        write(String.format(format, args));
    }
}
