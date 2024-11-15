package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to display all elements in the collection.
 * <p>
 * The {@code Show} command retrieves all elements from the collection managed by
 * {@link CollectionManager} and returns them in a {@link Response}.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class Show extends Command {

    /**
     * Constructs a new {@code Show} command.
     * <p>
     * The command is named "show" and requires no arguments.
     * </p>
     */
    Show() {
        super("show", "shows elements of the collection");
    }

    /**
     * Executes the command to display all elements in the collection.
     * <p>
     * If the collection is empty, a response indicating this is returned. Otherwise,
     * all elements in the collection are included in the response.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} containing the collection elements or an appropriate message
     * if the collection is empty
     * @check if the collection is not empty before returning the elements
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Collection is empty.");
        }
        return new Response("Elements of the collection:", CollectionManager.getInstance().list());
    }
}
