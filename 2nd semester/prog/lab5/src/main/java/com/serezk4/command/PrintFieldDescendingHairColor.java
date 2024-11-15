package com.serezk4.command;

import com.serezk4.collection.CollectionManager;
import com.serezk4.collection.model.Person;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

/**
 * Command to print the hair color field of all persons in descending order.
 * <p>
 * The {@code PrintFieldDescendingHairColor} command retrieves the hair colors of all persons
 * in the collection managed by {@link CollectionManager}, sorts them in descending order,
 * and returns the result in a {@link Response}.
 * </p>
 *
 * @see CollectionManager
 * @see Person
 * @since 1.0
 */
public final class PrintFieldDescendingHairColor extends Command {

    /**
     * Constructs a new {@code PrintFieldDescendingHairColor} command.
     * <p>
     * The command is named "print_field_descending_hair_color" and requires no arguments.
     * </p>
     */
    PrintFieldDescendingHairColor() {
        super("print_field_descending_hair_color", "print field hair color in descending order");
    }

    /**
     * Executes the command to print the hair color field in descending order.
     * <p>
     * If the collection is empty, a response indicating this is returned. Otherwise,
     * the hair colors are sorted in descending order and included in the response.
     * </p>
     *
     * @param request the request to execute (unused for this command)
     * @return a {@link Response} containing the sorted hair colors or an appropriate message
     * if the collection is empty
     */
    @Override
    public Response execute(final Request request) {
        if (CollectionManager.getInstance().list().isEmpty()) {
            return new Response("Collection is empty.");
        }
        return new Response("Field hair color in descending order: %s".formatted(
                CollectionManager.getInstance().list().stream()
                        .sorted((o1, o2) -> o2.getHairColor().compareTo(o1.getHairColor()))
                        .map(Person::getHairColor)
                        .toList()
        ));
    }
}
