package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.collection.model.Person;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to get sum of height of all elements
 *
 * @see CollectionManager
 */
public class SumOfHeight extends Command {
    protected SumOfHeight() {
        super("sum_of_height", "sum of height of all elements");
    }

    /**
     * Gets sum of height of all elements
     * @param request request to execute
     * @return response with sum of height
     * <p>
     * @check if CollectionManager.getInstance().list() is not empty
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Collection is empty.");
        return new Response("Sum of height: %d".formatted(CollectionManager.getInstance().list().stream().mapToInt(Person::getHeight).sum()));
    }
}
