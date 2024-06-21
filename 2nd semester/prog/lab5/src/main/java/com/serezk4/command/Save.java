package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to save collection to file
 *
 * @see CollectionManager
 */
public class Save extends Command {
    protected Save() {
        super("save","save collection to file");
    }

    /**
     * Saves collection to file
     *
     * @param request request to execute
     * @return response says that collection is saved
     */
    @Override
    public Response execute(Request request) {
        CollectionManager.getInstance().save();
        return new Response("Collection saved.");
    }
}
