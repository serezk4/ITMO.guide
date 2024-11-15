package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to clear the entire collection.
 * <p>
 * The {@code Clear} command removes all elements from the collection managed by
 * {@link CollectionManager}.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class Clear extends Command {

    /**
     * Constructs a new {@code Clear} command.
     * <p>
     * The command is named "clear" and requires no arguments.
     * </p>
     */
    Clear() {
        super("clear", "clear all collection");
    }

    /**
     * Executes the command to clear the collection.
     * <p>
     * If the collection is already empty, a response indicating the empty state is returned.
     * Otherwise, the collection is cleared, and a success message is returned.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} indicating whether the collection was cleared or was already empty
     */
    @Override
    public Response execute(final Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Sorry! Collection is empty.");
        }
        CollectionManager.getInstance().list().clear();
        return new Response("Collection cleared.");
    }
}
