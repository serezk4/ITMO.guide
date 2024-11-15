package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to remove all elements from the collection that are greater than the specified element.
 * <p>
 * The {@code RemoveGreater} command compares all elements in the collection managed by
 * {@link CollectionManager} to a given {@link com.serezk4.database.model.Person}. All elements
 * greater than the specified element are removed.
 * </p>
 *
 * @see CollectionManager
 * @since 1.0
 */
public final class RemoveGreater extends Command {

    /**
     * Constructs a new {@code RemoveGreater} command.
     * <p>
     * The command is named "remove_greater" and requires one {@link com.serezk4.database.model.Person}
     * as input to compare with the elements in the collection.
     * </p>
     */
    RemoveGreater() {
        super(
                "remove_greater",
                Command.EMPTY_ARGS,
                "remove all elements greater than given",
                1
        );
    }

    /**
     * Executes the command to remove all elements greater than the specified element.
     * <p>
     * If the request does not contain a person to compare or the collection is empty, an appropriate
     * response is returned. Otherwise, elements greater than the specified person are removed
     * from the collection.
     * </p>
     *
     * @param request the request containing the {@link com.serezk4.database.model.Person} to compare
     * @return a {@link Response} indicating whether the elements were successfully removed or if
     * no operation was performed
     * @check if {@code request.persons()} is null or empty
     * @check if the collection is not empty before attempting to remove elements
     */
    @Override
    public Response execute(final Request request) {
        if (request.persons() == null || request.persons().isEmpty()) {
            return new Response("No persons to compare.");
        }
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Collection is empty.");
        }
        CollectionManager.getInstance().list().removeIf(person -> person.compareTo(request.persons().get(0)) > 0);
        return new Response("Persons that are greater than the given element successfully removed.");
    }
}
