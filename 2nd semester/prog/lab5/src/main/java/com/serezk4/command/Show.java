package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to show elements of the collection
 *
 * @see CollectionManager
 */
public class Show extends Command {
    protected Show() {
        super("show", "shows elements of the collection");
    }

    /**
     * Shows elements of the collection
     *
     * @param request request to execute
     * @return elements of the collection if there are any
     * <p>
     * @check if CollectionManager.getInstance().list() is not empty
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Collection is empty.");
        return new Response("Elements of the collection:", CollectionManager.getInstance().list());
    }
}
