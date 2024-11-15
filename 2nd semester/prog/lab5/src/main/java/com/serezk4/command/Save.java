package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to save the current state of the collection to a file.
 * <p>
 * The {@code Save} command invokes the {@link CollectionManager#save()} method to persist
 * the current state of the collection to its associated file.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class Save extends Command {

    /**
     * Constructs a new {@code Save} command.
     * <p>
     * The command is named "save" and requires no arguments.
     * </p>
     */
    Save() {
        super("save", "save collection to file");
    }

    /**
     * Executes the command to save the collection to a file.
     * <p>
     * This method calls the {@link CollectionManager#save()} method to persist the collection
     * and returns a {@link Response} indicating success.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} confirming that the collection has been saved
     */
    @Override
    public Response execute(Request request) {
        CollectionManager.getInstance().save();
        return new Response("Collection saved.");
    }
}
