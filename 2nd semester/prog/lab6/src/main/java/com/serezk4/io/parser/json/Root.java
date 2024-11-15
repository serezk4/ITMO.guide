package com.serezk4.io.parser.json;

import com.serezk4.collection.model.Person;

import java.util.List;

/**
 * Represents the root structure of a JSON file containing a collection of {@link Person} objects.
 * <p>
 * This record encapsulates the top-level JSON structure used for serializing and deserializing
 * a collection of {@link Person} instances.
 * </p>
 *
 * @param collection the list of {@link Person} objects
 * @see Person
 * @since 1.0
 */
public record Root(List<Person> collection) {

}
