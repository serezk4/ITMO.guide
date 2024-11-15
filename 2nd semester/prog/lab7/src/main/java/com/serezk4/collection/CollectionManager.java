package com.serezk4.collection;

import com.serezk4.collection.model.Person;
import com.serezk4.configuration.FileConfiguration;
import com.serezk4.io.parser.Formatter;
import com.serezk4.io.parser.json.JsonFormatter;

import java.util.LinkedList;
import java.util.List;

/**
 * Manages the collection of {@link Person} objects.
 * <p>
 * This class is implemented as a singleton and provides methods to load and save
 * a collection of persons from and to a file, as well as access the current list
 * of persons in memory.
 * </p>
 *
 * @see Person
 * @see Formatter
 * @see JsonFormatter
 * @see FileConfiguration
 * @since 1.0
 */
public class CollectionManager {
    private static CollectionManager instance;

    private final List<Person> list = new LinkedList<>();

    /**
     * Private constructor to enforce singleton pattern.
     * <p>
     * Initializes the manager by automatically loading persons from the configured file.
     * </p>
     */
    private CollectionManager() {
        load();
    }

    /**
     * Returns the singleton instance of {@code CollectionManager}.
     * <p>
     * If the instance does not exist, it is created. Otherwise, the existing
     * instance is returned.
     * </p>
     *
     * @return the singleton instance of {@code CollectionManager}
     */
    public static CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    /**
     * Loads the collection of persons from the configured file.
     * <p>
     * Clears the current list of persons and populates it with the data read
     * from the file. Any errors encountered during loading are logged to the
     * error stream.
     * </p>
     */
    public void load() {
        try (Formatter<Person> formatter = new JsonFormatter(FileConfiguration.DATA_FILE_PATH)) {
            list.clear();
            list.addAll(formatter.read());
            System.out.printf("loaded %d elements%n", list.size());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Saves the collection of persons to the configured file.
     * <p>
     * Writes the current list of persons to the file using the specified formatter.
     * Any errors encountered during saving are logged to the error stream.
     * </p>
     */
    public void save() {
        try (Formatter<Person> formatter = new JsonFormatter(FileConfiguration.DATA_FILE_PATH)) {
            formatter.write(list);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns the current list of persons.
     * <p>
     * The returned list reflects the collection as currently loaded in memory.
     * </p>
     *
     * @return a {@link List} of {@link Person} objects
     */
    public List<Person> list() {
        return list;
    }
}