package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to remove all elements greater than given
 *
 * @see CollectionManager
 */
public class RemoveGreater extends Command {
    protected RemoveGreater() {
        super("remove_greater", Command.EMPTY_ARGS, "remove all elements greater than given", 1);
    }

    /**
     * Removes all elements greater than given
     * @param request request to execute
     * @return response says that elements are removed
     * <p>
     * @check if request.persons() is null or empty
     * @check if CollectionManager.getInstance().list() is not empty
     */
    @Override
    public Response execute(Request request) {
        if (request.persons() == null || request.persons().isEmpty()) return new Response("No persons to compare.");
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Collection is empty.");
        CollectionManager.getInstance().list().removeIf(person -> person.compareTo(request.persons().get(0)) > 0);
        return new Response("Persons that are greater than given successfully removed.");
    }
}
