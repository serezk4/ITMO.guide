package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to add an element to the collection.
 * <p>
 * The {@code Add} command is used to add a single {@link com.serezk4.collection.model.Person}
 * to the collection managed by {@link CollectionManager}.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class Add extends Command {

    /**
     * Constructs a new {@code Add} command.
     * <p>
     * The command is named "add" and expects exactly one {@link com.serezk4.collection.model.Person}
     * to be provided in the {@link Request}.
     * </p>
     */
    Add() {
        super("add", Command.EMPTY_ARGS, "adds element to the collection", 1);
    }

    /**
     * Executes the command to add an element to the collection.
     * <p>
     * If the request does not contain any persons to add, a response with an appropriate
     * message is returned. Otherwise, the first person in the request is added to the
     * collection, and a success response is returned.
     * </p>
     *
     * @param request the request containing the {@link com.serezk4.collection.model.Person} to add
     * @return a {@link Response} indicating the result of the operation
     */
    @Override
    public Response execute(final Request request) {
        if (request.persons() == null || request.persons().isEmpty()) {
            return new Response("No persons to add.");
        }
        CollectionManager.getInstance().list().add(request.persons().get(0));
        return new Response("Person added.");
    }
}
