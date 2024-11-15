package com.serezk4.io.trasnfer;

import com.serezk4.collection.model.Person;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a request containing a command, its arguments, and associated persons.
 * <p>
 * This record encapsulates all necessary data to execute a command, including the command
 * name, optional arguments, and a list of {@link Person} objects.
 * </p>
 *
 * @param command the name of the command to execute
 * @param args    the arguments required for the command
 * @param persons the list of {@link Person} objects associated with the command
 * @see Person
 * @since 1.0
 */
public record Request(String command, List<String> args, List<Person> persons) implements Serializable {
}
