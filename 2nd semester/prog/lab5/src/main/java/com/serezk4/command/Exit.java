package com.serezk4.command;

import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to exit the program
 */
public class Exit extends Command {
    protected Exit() {
        super("exit", "exits the program");
    }

    /**
     * Exits the program
     *
     * @param request request to execute
     * @return response to the request
     */
    @Override
    public Response execute(Request request) {
        System.exit(0);
        return new Response("Exiting...");
    }
}
