package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to display the first element of the collection.
 * <p>
 * The {@code Head} command retrieves and displays the first element in the collection managed by
 * {@link CollectionManager}. If the collection is empty, an appropriate message is returned.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class Head extends Command {

    /**
     * Constructs a new {@code Head} command.
     * <p>
     * The command is named "head" and requires no arguments.
     * </p>
     */
    Head() {
        super("head", "show first elements of collection");
    }

    /**
     * Executes the command to display the first element of the collection.
     * <p>
     * If the collection is empty, a response indicating this is returned. Otherwise,
     * the first element of the collection is returned in the response.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} containing the first element of the collection or an
     * appropriate message if the collection is empty
     */
    @Override
    public Response execute(final Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Collection is empty.");
        }
        return new Response("First element of collection", CollectionManager.getInstance().list().get(0));
    }
}
