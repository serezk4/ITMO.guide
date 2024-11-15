package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to remove an element from the collection by its ID.
 * <p>
 * The {@code RemoveById} command searches for an element with the specified ID in the collection
 * managed by {@link CollectionManager} and removes it if found.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class RemoveById extends Command {

    /**
     * Constructs a new {@code RemoveById} command.
     * <p>
     * The command is named "remove_by_id" and requires a single argument: the ID of the element to remove.
     * </p>
     */
    RemoveById() {
        super("remove_by_id", new String[]{"id"}, "removes element by id");
    }

    /**
     * Executes the command to remove an element by its ID.
     * <p>
     * Validates the provided ID and checks whether an element with the given ID exists in the collection.
     * If the ID is invalid or no such element exists, an appropriate response is returned. If the element
     * is found, it is removed, and a success response is returned.
     * </p>
     *
     * @param request the request containing the ID as an argument
     * @return a {@link Response} indicating the result of the operation
     * @check if {@code request.args()} is null or empty
     * @check if {@code request.args().get(0)} matches the regex for a valid integer ID
     * @check if the collection contains an element with the specified ID
     */
    @Override
    public Response execute(final Request request) {
        if (request.args() == null || request.args().isEmpty()) {
            return new Response("No id to remove.");
        }
        if (!request.args().get(0).matches("^(0|[1-9]\\d{0,9}|214748364[0-7])$")) {
            return new Response("Invalid id");
        }

        final int targetId = Integer.parseInt(request.args().get(0));
        if (CollectionManager.getInstance().list().stream().noneMatch(person -> person.getId() == targetId)) {
            return new Response("Person with id %d not found.".formatted(targetId));
        }
        CollectionManager.getInstance().list().removeIf(person -> person.getId() == targetId);

        return new Response("Person removed. Removed element:");
    }
}
