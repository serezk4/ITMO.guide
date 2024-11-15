package com.serezk4.io.trasnfer;

import com.serezk4.database.model.Person;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a response containing a message, a list of persons, and an optional script.
 * <p>
 * This record is used to encapsulate the result of an operation, including optional
 * metadata or additional instructions (e.g., a script to execute).
 * </p>
 *
 * @param message the response message, providing details about the result or status
 * @param persons the list of {@link Person} objects associated with the response
 * @param script  an optional script to execute (used for specific operations)
 * @since 1.0
 */
public record Response(String message, List<Person> persons, String script) implements Serializable {

    /**
     * Constructs a {@code Response} with a message and a list of persons.
     *
     * @param message the response message
     * @param persons the list of {@link Person} objects
     */
    public Response(
            final String message,
            final List<Person> persons
    ) {
        this(message, persons, null);
    }

    /**
     * Constructs a {@code Response} with a message and a variable number of persons.
     *
     * @param message the response message
     * @param persons the {@link Person} objects
     */
    public Response(
            final String message,
            final Person... persons
    ) {
        this(message, Arrays.asList(persons), null);
    }

    /**
     * Constructs a {@code Response} with only a message.
     *
     * @param message the response message
     */
    public Response(final String message) {
        this(message, Collections.emptyList(), null);
    }

    /**
     * Returns an empty {@code Response}.
     * <p>
     * This method provides a pre-defined response with no message, persons, or script.
     * </p>
     *
     * @return an empty {@code Response} instance
     */
    public static Response empty() {
        return new Response(null);
    }
}
