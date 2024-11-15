package com.serezk4.command;

import java.util.List;

/**
 * Holds the active list of commands available in the application.
 * <p>
 * The {@code Active} class is a utility class that provides a static, immutable list
 * of all {@link Command} instances used by the application. This class cannot be instantiated.
 * </p>
 *
 * @see Command
 * @since 1.0
 */
public final class Active {

    /**
     * A static, immutable list of active commands.
     * <p>
     * This list includes all commands available in the application, such as {@code Add},
     * {@code Clear}, {@code Exit}, and others.
     * </p>
     */
    public static final List<Command> LIST = List.of(
            new Add(),
            new Clear(),
            new ExecuteScript(),
            new Exit(),
            new Head(),
            new PrintFieldDescendingHairColor(),
            new RemoveById(),
            new RemoveFirst(),
            new RemoveGreater(),
            new Save(),
            new Show(),
            new SumOfHeight()
    );

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This class is intended to be used as a static utility for accessing the active commands.
     * </p>
     */
    private Active() {
    }
}
