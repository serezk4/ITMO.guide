package com.serezk4.collection;

import com.serezk4.collection.model.Person;

import java.util.LinkedList;
import java.util.List;

/**
 * CollectionManager class.
 * SINGLETON
 */
public class CollectionManager {
    private static CollectionManager instance;

    private final List<Person> list = new LinkedList<>();

    /**
     * private CollectionManager constructor.
     */
    private CollectionManager() {}

    /**
     * Get instance of CollectionManager.
     * @return Instance of CollectionManager.
     */
    public static CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    /**
     * Get list of persons.
     *
     * @return List of persons.
     * @see Person
     */
    public List<Person> list() {
        return list;
    }
}
