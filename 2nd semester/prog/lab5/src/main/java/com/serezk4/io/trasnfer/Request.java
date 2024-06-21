package com.serezk4.io.trasnfer;

import com.serezk4.collection.model.Person;

import java.util.List;

/**
 * Request class.
 *
 * @param command Command name
 * @param args    Arguments for the command
 * @param persons List of persons for the command
 */
public record Request(String command, List<String> args, List<Person> persons) {}
