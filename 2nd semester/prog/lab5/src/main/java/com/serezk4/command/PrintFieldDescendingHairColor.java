package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.collection.model.Person;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to print field hair color in descending order
 *
 * @see CollectionManager
 */
public class PrintFieldDescendingHairColor extends Command {
    protected PrintFieldDescendingHairColor() {
        super("print_field_descending_hair_color", "print field hair color in descending order");
    }

    /**
     * Prints field hair color in descending order
     * @param request request to execute
     * @return response with field hair color in descending order
     */
    @Override
    public Response execute(Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) return new Response("Collection is empty.");
        return new Response("Field hair color in descending order: %s".formatted(
                CollectionManager.getInstance().list().stream()
                        .sorted((o1, o2) -> o2.getHairColor().compareTo(o1.getHairColor()))
                        .map(Person::getHairColor)
                        .toList()
        ));
    }
}
