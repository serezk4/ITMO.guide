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
 * Handler class.
 * Worker for handling input.
 */
public class Handler implements Runnable {
    private final ConsoleWorker console;
    private final IOWorker<String> script;

    private final Router router = new Router();

    private int recursionDepth;

    /**
     * Basic constructor.
     *
     * @param console console worker
     * @param script  script worker
     */
    public Handler(ConsoleWorker console, IOWorker<String> script) {
        this.console = console;
        this.script = script;
    }

    /**
     * Run the handler.
     */
    @Override
    public void run() {
        console.writeln("welcome to lab5!");
        CollectionManager.getInstance().load();

        try {
            String line;
            while ((line = console.read("")) != null) {
                recursionDepth = 0;
                handle(line);
                while (!script.ready()) handle(script.read());
            }
        } catch (Exception someShit) {
            console.writef("error: %s%n", someShit.getMessage());
        }
    }

    /**
     * Handle the input line.
     *
     * @param line input line
     */
    private void handle(String line) {
        if (line == null) return;
        print(router.route(parse(line)));
    }

    /**
     * Print the response.
     *
     * @param response response to print
     */
    private void print(Response response) {
        if (response.message() != null && !response.message().isBlank()) console.writeln(response.message());
        if (response.persons() != null && !response.persons().isEmpty()) response.persons().stream().map(Person::toString).forEach(console::writeln);
        if (response.script() != null && !response.script().isEmpty()) script.insert(response.script());
    }

    /**
     * Parse the input line.
     *
     * @param line raw input line
     * @return line parsed to request
     * <p>
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
