package com.serezk4.chat;

import com.serezk4.command.Active;
import com.serezk4.command.Command;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Routes requests to the appropriate command for execution.
 * <p>
 * This class acts as a central hub for processing incoming requests by matching
 * them to the corresponding command and executing it. It also provides functionality
 * to retrieve help information about available commands.
 * </p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class Router {

    /**
     * The single instance of the {@code Router} class.
     * <p>
     * This field is lazily initialized and will hold the only instance of {@code Router}.
     * </p>
     */
    private static Router instance;

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * The constructor is private to enforce the Singleton design pattern, ensuring that
     * the {@code Router} class cannot be instantiated directly.
     * </p>
     */
    private Router() {
    }

    /**
     * Returns the single instance of the {@code Router} class.
     * <p>
     * If the instance does not already exist, it is created and returned. Subsequent calls
     * to this method will return the same instance.
     * </p>
     *
     * @return the single instance of the {@code Router} class
     */
    public static Router getInstance() {
        return instance == null ? instance = new Router() : instance;
    }

    /**
     * Routes the specified {@link Request} to the corresponding {@link Command}.
     * <p>
     * If the command in the request is invalid or not found, an error response
     * is returned. If the "help" command is requested, a list of available commands
     * is returned.
     * </p>
     *
     * @param request the request to route
     * @return the {@link Response} from the executed command
     */
    public Response route(Request request) {
        if (request == null || request.command() == null || request.command().isBlank()) return Response.empty();
        if (request.command().equals("help")) return getHelp();

        return Active.LIST.stream()
                .filter(temp -> temp.getName().equalsIgnoreCase(request.command()))
                .findFirst()
                .map(temp -> temp.execute(request))
                .orElse(new Response("command '%s' not found, type 'help' for help".formatted(request.command())));
    }

    /**
     * Retrieves the number of elements required for a specific command.
     * <p>
     * Searches for the command in the active command list and returns the
     * number of elements it requires. If the command is not found, returns 0.
     * </p>
     *
     * @param command the name of the command
     * @return the number of elements required for the command
     */
    public int getElementsRequiredFor(String command) {
        return Active.LIST.stream()
                .filter(temp -> temp.getName().equalsIgnoreCase(command)).findFirst()
                .map(Command::getElementsRequired).orElse(0);
    }

    /**
     * Retrieves help information for all available commands.
     * <p>
     * Constructs a response containing a list of all commands, their arguments,
     * and descriptions for user reference.
     * </p>
     *
     * @return a {@link Response} containing help information
     */
    private Response getHelp() {
        return new Response("Available commands:%s".formatted(
                Active.LIST.stream()
                        .map(command -> "%n ~ %s %s - %s".formatted(
                                command.getName(),
                                Arrays.toString(command.getArgs()),
                                command.getHelp()
                        ))
                        .collect(Collectors.joining()))
        );
    }
}
