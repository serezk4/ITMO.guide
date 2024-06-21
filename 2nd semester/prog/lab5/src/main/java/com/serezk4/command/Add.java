package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to add element to the collection
 *
 * @see CollectionManager
 */
public class Add extends Command {
    protected Add() {
        super("add", Command.EMPTY_ARGS, "adds element to the collection", 1);
    }

    /**
     * Adds element to the collection
     *
     * @param request request to execute
     * @return response to the request
     * <p>
     * @check if request.persons() is null or empty
     */
    @Override
    public Response execute(Request request) {
        if (request.persons() == null || request.persons().isEmpty()) return new Response("No persons to add.");
        CollectionManager.getInstance().list().add(request.persons().get(0));
        return new Response("Person added.");
    }
}
