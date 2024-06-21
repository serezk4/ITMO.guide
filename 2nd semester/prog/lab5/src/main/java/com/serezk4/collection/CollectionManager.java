package com.serezk4.collection;

import com.serezk4.collection.model.Person;
import com.serezk4.configuration.FileConfiguration;
import com.serezk4.io.parser.Formatter;
import com.serezk4.io.parser.json.JsonFormatter;

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
    private CollectionManager() {
        load();
    }

    /**
     * Get instance of CollectionManager.
     * @return Instance of CollectionManager.
     */
    public static CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    /**
     * Load persons from file.
     */
    public void load() {
        try(Formatter<Person> formatter = new JsonFormatter(FileConfiguration.DATA_FILE_PATH)) {
            list.clear();
            list.addAll(formatter.read());
            System.out.printf("loaded %d elements%n", list.size());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Save persons to file.
     */
    public void save() {
        try(Formatter<Person> formatter = new JsonFormatter(FileConfiguration.DATA_FILE_PATH)) {
            formatter.write(list);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
