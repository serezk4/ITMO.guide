package com.serezk4.collection.id;

import com.serezk4.configuration.FileConfiguration;
import com.serezk4.io.file.BufferedFileWorker;
import com.serezk4.io.file.FileWorker;

import java.io.IOException;

/**
 * Singleton class for generating unique IDs.
 * <p>
 * The {@code IdGenerator} class is responsible for generating unique incremental IDs
 * and persisting the current state to a file. This ensures IDs remain consistent
 * across application restarts.
 * </p>
 *
 * @see FileWorker
 * @see BufferedFileWorker
 * @see FileConfiguration
 * @since 1.0
 */
public class IdGenerator {
    private static IdGenerator instance = null;

    private final FileWorker fileWorker;
    private int currentId;

    /**
     * Private constructor for initializing the {@code IdGenerator}.
     * <p>
     * Sets up the file worker to read and write IDs from/to the configured file path
     * and initializes the current ID.
     * </p>
     *
     * @throws IOException if an error occurs during file operations
     */
    private IdGenerator() throws IOException {
        this.fileWorker = new BufferedFileWorker(FileConfiguration.ID_FILE_PATH, true);
        initialize();
    }

    /**
     * Returns the singleton instance of the {@code IdGenerator}.
     * <p>
     * If the instance does not already exist, it is created. Otherwise, the existing
     * instance is returned.
     * </p>
     *
     * @return the singleton instance of {@code IdGenerator}
     * @throws IOException if an error occurs during initialization
     */
    public static synchronized IdGenerator getInstance() throws IOException {
        return instance == null ? instance = new IdGenerator() : instance;
    }

    /**
     * Initializes the current ID by reading it from the file.
     * <p>
     * If the file does not contain a valid ID, the current ID is reset to 0.
     * </p>
     */
    private void initialize() {
        try {
            String lastIdStr = fileWorker.read();
            currentId = lastIdStr != null ? Integer.parseInt(lastIdStr) : 0;
        } catch (NumberFormatException e) {
            fileWorker.write(Long.toString(currentId = 0));
        }
    }

    /**
     * Generates a new unique ID and saves it to the file.
     * <p>
     * The method increments the current ID, persists it to the file, and then returns
     * the new value.
     * </p>
     *
     * @return the newly generated unique ID
     * @throws IOException if an error occurs during file operations
     */
    public synchronized int generateId() throws IOException {
        currentId++;
        fileWorker.write(Long.toString(currentId) + System.lineSeparator());
        return currentId;
    }

    /**
     * Closes the {@link FileWorker}.
     * <p>
     * This method ensures proper release of resources used by the file worker.
     * </p>
     *
     * @throws Exception if an error occurs during file closure
     */
    public void close() throws Exception {
        fileWorker.close();
    }
}
