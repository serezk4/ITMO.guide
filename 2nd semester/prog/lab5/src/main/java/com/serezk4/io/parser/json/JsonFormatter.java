package com.serezk4.io.parser.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.serezk4.collection.model.Person;
import com.serezk4.io.file.BufferedFileWorker;
import com.serezk4.io.file.FileWorker;
import com.serezk4.io.parser.Formatter;
import com.serezk4.io.parser.json.adapters.IgnoreFailureTypeAdapterFactory;
import com.serezk4.io.parser.json.adapters.LocalDateAdapter;
import com.serezk4.io.parser.json.adapters.ResourceDeserializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A JSON-based implementation of the {@link Formatter} interface for handling collections of {@link Person}.
 * <p>
 * This class uses Gson for serialization and deserialization of {@link Person} objects and provides
 * methods to read collections from and write collections to JSON files.
 * </p>
 *
 * @see Formatter
 * @see Person
 * @see Gson
 * @see BufferedFileWorker
 * @since 1.0
 */
public class JsonFormatter implements Formatter<Person> {

    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .registerTypeAdapterFactory(new IgnoreFailureTypeAdapterFactory())
            .registerTypeAdapter(LinkedList.class, new ResourceDeserializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private final Path filePath;

    /**
     * Constructs a {@code JsonFormatter} for the specified file path.
     *
     * @param filePath the path to the JSON file
     */
    public JsonFormatter(final Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads a collection of {@link Person} objects from the JSON file.
     * <p>
     * If the file does not exist, is not readable, or is empty, an empty list is returned.
     * </p>
     *
     * @return a list of {@link Person} objects, or an empty list if the file cannot be read
     */
    @Override
    public List<Person> read() {
        if (Files.notExists(filePath)) {
            System.err.printf("File %s not found%n", filePath.getFileName());
            return Collections.emptyList();
        }

        if (!Files.isReadable(filePath)) {
            System.err.printf("File %s is not readable%n", filePath.getFileName());
            return Collections.emptyList();
        }

        try (FileWorker fileWorker = new BufferedFileWorker(filePath)) {
            Root root = gson.fromJson(fileWorker.read(), Root.class);

            if (root == null || root.collection() == null) {
                System.out.println("It seems the input file is empty. An empty collection will be initialized.");
                return Collections.emptyList();
            }

            System.out.println("Collection successfully initialized! Size: " + root.collection().size());
            return root.collection();
        } catch (Exception e) {
            System.err.printf("Error processing file %s%n%s%n", filePath.getFileName(), e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Writes a collection of {@link Person} objects to the JSON file.
     * <p>
     * If the file is not writable or does not exist, the operation will be aborted.
     * </p>
     *
     * @param values the collection of {@link Person} objects to write
     */
    @Override
    public void write(final List<Person> values) {
        if (Files.notExists(filePath)) {
            System.err.printf("File %s not found%n", filePath.getFileName());
            return;
        }

        if (!Files.isWritable(filePath)) {
            System.err.printf("Cannot write to %s%n", filePath.getFileName());
            return;
        }

        try (FileWorker fileWorker = new BufferedFileWorker(filePath, false)) {
            fileWorker.write(gson.toJson(new Root(values)));
        } catch (Exception e) {
            System.err.printf("Failed when trying to write to %s: %s%n", filePath.getFileName(), e.getMessage());
        }
    }

    /**
     * Checks if the file is ready for I/O operations.
     * <p>
     * Ensures the file exists, is not a directory, and is both readable and writable.
     * </p>
     *
     * @return {@code true} if the file is ready, {@code false} otherwise
     */
    @Override
    public boolean ready() {
        return !Files.isDirectory(filePath)
                && Files.exists(filePath)
                && Files.isReadable(filePath)
                && Files.isWritable(filePath);
    }

    /**
     * Closes the formatter.
     * <p>
     * This implementation does not require any specific cleanup, so this method is empty.
     * </p>
     */
    @Override
    public void close() {
        // Do nothing
    }
}
