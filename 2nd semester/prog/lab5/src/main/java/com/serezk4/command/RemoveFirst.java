package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to remove first element from collection
 *
 * @see CollectionManager
 */
public class RemoveFirst extends Command {
    protected RemoveFirst() {
        super("remove_first", "remove first element from collection");
    }

    /**
     * Removes first element from collection
     *
     * @param request request to execute
     * @return response says that first element is removed
     * <p>
     * @check if CollectionManager.getInstance().list() is not empty
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Collection is empty.");
        CollectionManager.getInstance().list().remove(0);
        return new Response("First element removed.");
    }
}
