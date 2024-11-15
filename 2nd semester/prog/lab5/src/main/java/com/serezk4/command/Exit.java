package com.serezk4.command;

import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to exit the program.
 * <p>
 * The {@code Exit} command terminates the program immediately upon execution.
 * </p>
 *
 * @since 1.0
 */
public final class Exit extends Command {

    /**
     * Constructs a new {@code Exit} command.
     * <p>
     * The command is named "exit" and requires no arguments.
     * </p>
     */
    Exit() {
        super("exit", "exits the program");
    }

    /**
     * Executes the command to exit the program.
     * <p>
     * This method terminates the program using {@link System#exit(int)}. Although it returns
     * a {@link Response} indicating that the program is exiting, the program will terminate
     * before the response can be processed.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} indicating the program is exiting (unreachable due to termination)
     */
    @Override
    public Response execute(final Request request) {
        System.exit(0);
        return new Response("Exiting...");
    }
}
