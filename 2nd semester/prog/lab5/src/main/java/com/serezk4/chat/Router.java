package com.serezk4.chat;

import com.serezk4.command.Active;
import com.serezk4.command.Command;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class that routes the request to the specified command
 */
public class Router {
    /**
     * Method to route the request to the command
     *
     * @param request request to route
     * @return response from the command
     */
    public Response route(Request request) {
        if (request == null || request.command() == null || request.command().isBlank()) return Response.empty();
        if (request.command().equals("help")) return getHelp();

        return Active.LIST.stream()
                .filter(temp -> temp.getName().equalsIgnoreCase(request.command())).findFirst()
                .map(temp -> temp.execute(request))
                .orElse(new Response("command '%s' not found, type 'help' for help".formatted(request.command())));
    }

    /**
     * Method to get how many elements required for command
     *
     * @param command command
     * @return elements required
     */
    public int getElementsRequiredFor(String command) {
        return Active.LIST.stream()
                .filter(temp -> temp.getName().equalsIgnoreCase(command)).findFirst()
                .map(Command::getElementsRequired)
                .orElse(0);
    }

    /**
     * Method to get help for all commands available with description
     *
     * @return response with help
     */
    private Response getHelp() {
        return new Response("Available commands:%s".formatted(
                Active.LIST.stream()
                        .map(command -> "%n ~ %s %s - %s".formatted(command.getName(), Arrays.toString(command.getArgs()), command.getHelp()))
                        .collect(Collectors.joining()))
        );
    }
}
