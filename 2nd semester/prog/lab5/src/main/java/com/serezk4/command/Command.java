package com.serezk4.command;

import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Abstract class that represents a command
 */
public abstract class Command {
    public static final String[] EMPTY_ARGS = new String[0];

    private final String name;
    private final String[] args;
    private final String help;
    private final int elementsRequired;

    /**
     * Constructor for the command
     *
     * @param name             name of the command
     * @param args             arguments of the command
     * @param help             help message of the command
     * @param elementsRequired number of elements required for the command
     */
    protected Command(String name, String[] args, String help, int elementsRequired) {
        this.name = name;
        this.args = args;
        this.help = help;
        this.elementsRequired = elementsRequired;
    }

    /**
     * Constructor for the command
     *
     * @param name name of the command
     * @param args arguments of the command
     * @param help help message of the command
     */
    public Command(String name, String[] args, String help) {
        this(name, args, help, 0);
    }

    /**
     * Constructor for the command
     *
     * @param name name of the command
     * @param help help message of the command
     */
    public Command(String name, String help) {
        this(name, EMPTY_ARGS, help);
    }

    /**
     * Executes the command
     *
     * @param request request to execute
     * @return response to the request
     * <p>
     * more details >>
     * @see Request
     * @see Response
     */
    public abstract Response execute(Request request);

    /**
     * Returns the name of the command
     *
     * @return name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the help message of the command
     *
     * @return help message of the command
     */
    public String getHelp() {
        return help;
    }

    /**
     * Returns the number of elements required for the command
     *
     * @return number of elements required for the command
     */
    public int getElementsRequired() {
        return elementsRequired;
    }

    /**
     * Returns the arguments of the command
     *
     * @return arguments of the command
     */
    public String[] getArgs() {
        return args;
    }
}
