package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to remove element by id
 *
 * @see CollectionManager
 */
public class RemoveById extends Command {
    protected RemoveById() {
        super("remove_by_id", new String[]{"id"}, "removes element by id");
    }

    /**
     * Removes element by id
     *
     * @param request request to execute
     * @return response to the request
     * <p>
     * @check if request.args() is null or empty
     * @check if request.args().get(0) matches "^(0|[1-9]\\d{0,9}|214748364[0-7])$" - regex for int
     * @check if CollectionManager.getInstance().list() contains element with id equal to request.args().get(0)
     */
    @Override
    public Response execute(Request request) {
        if (request.args() == null || request.args().isEmpty()) return new Response("No id to remove.");
        if (!request.args().get(0).matches("^(0|[1-9]\\d{0,9}|214748364[0-7])$")) return new Response("Invalid id");

        final int targetId = Integer.parseInt(request.args().get(0));
        if (CollectionManager.getInstance().list().stream().noneMatch(person -> person.getId() == targetId))
            return new Response("Person with id %d not found.".formatted(targetId));
        CollectionManager.getInstance().list().removeIf(person -> person.getId() == targetId);

        return new Response("Person removed. Removed element:");
    }
}
