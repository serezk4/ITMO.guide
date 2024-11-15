package com.serezk4;

import com.serezk4.chat.Handler;
import com.serezk4.io.IOWorker;
import com.serezk4.io.console.BufferedConsoleWorker;
import com.serezk4.io.console.ConsoleWorker;
import com.serezk4.io.deque.DequeWorker;

/**
 * The entry point of the program.
 * <p>
 * The {@code Main} class initializes the application by setting up the required input/output workers
 * and starting the {@link Handler} to manage the application logic. It employs a private constructor
 * to prevent instantiation and delegates the logic to the {@code run} method.
 * </p>
 *
 * @see Handler
 * @see ConsoleWorker
 * @see DequeWorker
 * @since 1.0
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This class is intended to be used statically via the {@link #main(String[])} method.
     * </p>
     */
    private Main() {
    }

    /**
     * The main method that starts the program.
     * <p>
     * Calls the {@link #run(String...)} method to initialize input and output workers and execute the
     * application logic using {@link Handler}.
     * </p>
     *
     * @param args command-line arguments (not used in this program)
     */
    public static void main(final String... args) {
        new Main().run(args);
    }

    /**
     * Initializes the application and executes the main logic.
     * <p>
     * Sets up a {@link BufferedConsoleWorker} for console input/output and a {@link DequeWorker}
     * for managing script execution. Passes these workers to the {@link Handler} instance for
     * managing the program's flow. Handles any exceptions by logging them to the error stream.
     * </p>
     *
     * @param args command-line arguments (not used in this program)
     */
    private void run(final String... args) {
        try (ConsoleWorker console = new BufferedConsoleWorker(); IOWorker<String> script = new DequeWorker()) {
            new Handler(console, script).run();
        } catch (Exception e) {
            System.err.printf("An error occurred: %s%n", e.getMessage());
        }
    }
}
