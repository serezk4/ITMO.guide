package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to clear all collection
 *
 * @see CollectionManager
 */
public class Clear extends Command {
    protected Clear() {
        super("clear", "clear all collection");
    }

    /**
     * Clears all collection
     *
     * @param request request to execute
     * @return response says that collection is cleared
     * <p>
     * @check if CollectionManager.getInstance().list() is not empty
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Sorry! Collection is empty.");
        CollectionManager.getInstance().list().clear();
        return new Response("Collection cleared.");
    }
}
