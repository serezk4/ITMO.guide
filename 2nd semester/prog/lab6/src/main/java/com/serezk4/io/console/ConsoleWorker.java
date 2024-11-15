package com.serezk4.io.console;

import com.serezk4.io.IOWorker;

/**
 * Interface for reading and writing data to and from the console.
 * <p>
 * Extends the {@link IOWorker} interface with methods tailored for console interactions,
 * such as formatted output and multi-line writing.
 * </p>
 *
 * @see IOWorker
 * @since 1.0
 */
public interface ConsoleWorker extends IOWorker<String> {

    /**
     * Writes a line of data to the console, appending a newline character.
     *
     * @param data the string to write
     */
    default void writeln(String data) {
        write(data.concat(System.lineSeparator()));
    }

    /**
     * Writes multiple lines of data to the console.
     * <p>
     * Each string in the {@code lines} array is written on a new line.
     * </p>
     *
     * @param lines an array of strings to write
     */
    default void writeln(String... lines) {
        write(String.join(System.lineSeparator(), lines).concat(System.lineSeparator()));
    }

    /**
     * Reads input from the console with a prompt.
     * <p>
     * Displays the specified prompt before waiting for input. The prompt is
     * followed by a "❯ " symbol.
     * </p>
     *
     * @param prompt the prompt to display
     * @return the input read from the console
     */
    @Override
    default String read(String prompt) {
        return IOWorker.super.read(prompt.concat(" ❯ "));
    }

    /**
     * Writes formatted data to the console.
     * <p>
     * Uses {@link String#format} to format the given string and arguments before writing.
     * </p>
     *
     * @param format the format string
     * @param args   the arguments referenced by the format string
     */
    default void writef(String format, Object... args) {
        write(String.format(format, args));
    }
}
