package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to remove the first element from the collection.
 * <p>
 * The {@code RemoveFirst} command removes the first element in the collection managed by
 * {@link CollectionManager}. If the collection is empty, an appropriate message is returned.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class RemoveFirst extends Command {

    /**
     * Constructs a new {@code RemoveFirst} command.
     * <p>
     * The command is named "remove_first" and requires no arguments.
     * </p>
     */
    RemoveFirst() {
        super("remove_first", "remove first element from collection");
    }

    /**
     * Executes the command to remove the first element from the collection.
     * <p>
     * If the collection is empty, a response indicating this is returned. Otherwise,
     * the first element is removed, and a success message is returned.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} indicating whether the first element was removed or the collection
     * was already empty
     * @check if the collection is empty before attempting to remove the first element
     */
    @Override
    public Response execute(final Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Collection is empty.");
        }
        CollectionManager.getInstance().list().remove(0);
        return new Response("First element removed.");
    }
}
