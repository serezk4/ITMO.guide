package com.serezk4.command;

import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Abstract base class representing a command.
 * <p>
 * A command encapsulates a specific operation that can be executed with a {@link Request}.
 * Each command has a name, optional arguments, a help message, and a requirement for
 * the number of elements needed to execute it.
 * </p>
 *
 * @see Request
 * @see Response
 * @since 1.0
 */
public abstract class Command {
    public static final String[] EMPTY_ARGS = new String[0];

    private final String name;
    private final String[] args;
    private final String help;
    private final int elementsRequired;

    /**
     * Constructs a command with all attributes specified.
     *
     * @param name             the name of the command
     * @param args             the arguments accepted by the command
     * @param help             the help message describing the command
     * @param elementsRequired the number of elements required for execution
     */
    protected Command(
            final String name,
            final String[] args,
            final String help,
            final int elementsRequired
    ) {
        this.name = name;
        this.args = args;
        this.help = help;
        this.elementsRequired = elementsRequired;
    }

    /**
     * Constructs a command with specified name, arguments, and help message.
     * <p>
     * The number of elements required defaults to 0.
     * </p>
     *
     * @param name the name of the command
     * @param args the arguments accepted by the command
     * @param help the help message describing the command
     */
    public Command(
            final String name,
            final String[] args,
            final String help
    ) {
        this(name, args, help, 0);
    }

    /**
     * Constructs a command with a name and help message.
     * <p>
     * The command accepts no arguments and requires no elements.
     * </p>
     *
     * @param name the name of the command
     * @param help the help message describing the command
     */
    public Command(
            final String name,
            final String help
    ) {
        this(name, EMPTY_ARGS, help);
    }

    /**
     * Executes the command with the specified {@link Request}.
     * <p>
     * Subclasses must implement this method to define the specific behavior of the command.
     * </p>
     *
     * @param request the request containing input data for the command
     * @return a {@link Response} representing the result of the execution
     */
    public abstract Response execute(Request request);

    /**
     * Returns the name of the command.
     *
     * @return the name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the help message for the command.
     *
     * @return the help message of the command
     */
    public String getHelp() {
        return help;
    }

    /**
     * Returns the number of elements required for the command to execute.
     *
     * @return the number of elements required
     */
    public int getElementsRequired() {
        return elementsRequired;
    }

    /**
     * Returns the arguments accepted by the command.
     *
     * @return an array of argument names
     */
    public String[] getArgs() {
        return args;
    }
}
