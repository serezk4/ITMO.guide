package com.serezk4.chat;

import com.serezk4.collection.CollectionManager;
import com.serezk4.collection.model.Person;
import com.serezk4.collection.util.InputUtil;
import com.serezk4.io.IOWorker;
import com.serezk4.io.console.ConsoleWorker;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.serezk4.collection.util.InputUtil.get;

/**
 * Handles user input and routes commands to the appropriate handlers.
 * <p>
 * This class is responsible for reading input from the user or script, parsing commands,
 * routing requests, and printing responses to the console. It serves as the main interface
 * between user input and command execution in the application.
 * </p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public class Handler implements Runnable {
    private final ConsoleWorker console;
    private final IOWorker<String> script;

    private final Router router = new Router();

    /**
     * Creates a new {@code Handler} instance.
     *
     * @param console the console worker for user interaction
     * @param script  the script worker for reading script inputs
     */
    public Handler(ConsoleWorker console, IOWorker<String> script) {
        this.console = console;
        this.script = script;
    }

    /**
     * Executes the main logic of the handler.
     * <p>
     * Loads the collection, welcomes the user, and processes input lines from the console
     * or script sequentially.
     * </p>
     */
    @Override
    public void run() {
        console.writeln("welcome to lab5!");
        CollectionManager.getInstance().load();

        try {
            String line;
            while ((line = console.read("")) != null) {
                handle(line);
                while (!script.ready()) handle(script.read());
            }
        } catch (Exception e) {
            console.writef("error: %s%n", e.getMessage());
        }
    }

    /**
     * Processes a single input line.
     * <p>
     * Parses the line into a {@link Request}, routes it to the appropriate handler,
     * and prints the resulting {@link Response}.
     * </p>
     *
     * @param line the input line to process
     */
    private void handle(String line) {
        if (line == null) return;
        print(router.route(parse(line)));
    }

    /**
     * Prints a {@link Response} to the console.
     * <p>
     * Outputs messages, persons, or scripts provided in the response to the console
     * or script worker.
     * </p>
     *
     * @param response the response to print
     */
    private void print(Response response) {
        if (response.message() != null && !response.message().isBlank()) console.writeln(response.message());
        if (response.persons() != null && !response.persons().isEmpty())
            response.persons().stream().map(Person::toString).forEach(console::writeln);
        if (response.script() != null && !response.script().isEmpty()) script.insert(response.script());
    }

    /**
     * Parses an input line into a {@link Request}.
     * <p>
     * Splits the input into command, arguments, and collects any required
     * {@link Person} objects from the input source.
     * </p>
     *
     * @param line the raw input line
     * @return a {@link Request} object containing parsed data
     * @see Request
     * @see InputUtil
     */
    private Request parse(String line) {
        final String[] parts = line.split(" ", 2);

        final String command = parts[0];
        final List<String> arguments = parts.length > 1 ? Arrays.asList(parts[1].split(" ")) : Collections.emptyList();
        final List<Person> persons = new LinkedList<>();

        int elementsRequired = router.getElementsRequiredFor(command);

        while (elementsRequired-- > 0) {
            try {
                persons.add(get(script.ready() ? console : script));
            } catch (InterruptedException ex) {
                console.writeln("command interrupted: %s".formatted(ex.getMessage()));
                return null;
            }
        }

        return new Request(command, arguments, persons);
    }
}