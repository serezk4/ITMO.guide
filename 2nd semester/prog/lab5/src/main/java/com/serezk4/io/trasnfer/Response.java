package com.serezk4.io.trasnfer;

import com.serezk4.collection.model.Person;

import java.util.*;

/**
 * Response class.
 *
 * @param message Response message.
 * @param persons List of persons.
 * @param script  script to execute (for exec)
 */
public record Response(String message, List<Person> persons, String script) {

    /**
     * Constructor for the response.
     *
     * @param message Response message.
     * @param persons List of persons.
     */
    public Response(String message, List<Person> persons) {
        this(message, persons, null);
    }

    /**
     * Constructor for the response.
     *
     * @param message Response message.
     * @param persons List of persons.
     */
    public Response(String message, Person... persons) {
        this(message, Arrays.asList(persons), null);
    }

    /**
     * Constructor for the response.
     *
     * @param message Response message.
     */
    public Response(String message) {
        this(message, Collections.emptyList(), null);
    }

    /**
     * Constructor for empty response.
     * @return empty response
     */
    public static Response empty() {
        return new Response(null);
    }
}
