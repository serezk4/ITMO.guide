package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.collection.model.Person;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to calculate the sum of the heights of all elements in the collection.
 * <p>
 * The {@code SumOfHeight} command computes the total height of all {@link Person} objects
 * in the collection managed by {@link CollectionManager}.
 * </p>
 *
 * @see CollectionManager
 * @see Person
 * @since 1.0
 */
public final class SumOfHeight extends Command {

    /**
     * Constructs a new {@code SumOfHeight} command.
     * <p>
     * The command is named "sum_of_height" and requires no arguments.
     * </p>
     */
    SumOfHeight() {
        super("sum_of_height", "sum of height of all elements");
    }

    /**
     * Executes the command to calculate the sum of the heights of all elements.
     * <p>
     * If the collection is empty, a response indicating this is returned. Otherwise,
     * the sum of heights is calculated and included in the response.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} containing the sum of the heights or an appropriate message
     * if the collection is empty
     * @check if the collection is not empty before calculating the sum
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Collection is empty.");
        }
        int sumOfHeights = CollectionManager.getInstance().list().stream()
                .mapToInt(Person::getHeight)
                .sum();
        return new Response("Sum of height: %d".formatted(sumOfHeights));
    }
}
