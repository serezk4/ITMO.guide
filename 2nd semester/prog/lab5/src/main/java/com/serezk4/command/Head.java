package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to show first elements of collection
 *
 * @see CollectionManager
 */
public class Head extends Command {
    protected Head() {
        super("head", "show first elements of collection");
    }

    /**
     * Shows first elements of collection
     *
     * @param request request to execute
     * @return shows first elements of collection
     * <p>
     * @check if CollectionManager.getInstance().list() is not empty
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Collection is empty.");
        return new Response("First element of collection", CollectionManager.getInstance().list().get(0));
    }
}
