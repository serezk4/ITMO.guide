package com.serezk4.io.parser.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.serezk4.collection.model.Person;
import com.serezk4.io.file.BufferedFileWorker;
import com.serezk4.io.file.FileWorker;
import com.serezk4.io.parser.Formatter;
import com.serezk4.io.parser.json.adapters.IgnoreFailureTypeAdapterFactory;
import com.serezk4.io.parser.json.adapters.ResourceDeserializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for reading and writing collection from/to file in JSON format
 *
 * @see Formatter
 */
public class JsonFormatter implements Formatter<Person> {
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .registerTypeAdapterFactory(new IgnoreFailureTypeAdapterFactory())
            .registerTypeAdapter(LinkedList.class, new ResourceDeserializer())
            .create();

    private final Path filePath;

    public JsonFormatter(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * read collection from file
     *
     * @return collection of persons
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
     * write collection to file
     *
     * @param values collection of persons
     */
    @Override
    public void write(List<Person> values) {
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

    @Override
    public boolean ready() {
        return !Files.isDirectory(filePath) && Files.exists(filePath) && Files.isReadable(filePath) && Files.isWritable(filePath);
    }

    @Override
    public void close() {
        // do nothing
    }
}
