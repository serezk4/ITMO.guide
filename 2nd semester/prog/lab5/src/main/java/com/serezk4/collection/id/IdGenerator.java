package com.serezk4.collection.id;

import com.serezk4.configuration.FileConfiguration;
import com.serezk4.io.file.BufferedFileWorker;
import com.serezk4.io.file.FileWorker;

import java.io.IOException;

/**
 * IdGenerator class.
 *
 * @see FileWorker
 * @see BufferedFileWorker
 */
public class IdGenerator {
    private static IdGenerator instance = null;

    private final FileWorker fileWorker;
    private int currentId;

    /**
     * Creates a new instance of IdGenerator with the specified file path.
     * @throws IOException if an I/O error occurs
     */
    private IdGenerator() throws IOException {
        this.fileWorker = new BufferedFileWorker(FileConfiguration.ID_FILE_PATH, true);
        initialize();
    }

    /**
     * Returns the instance of the IdGenerator class.
     * @return the instance of the IdGenerator class
     * @throws IOException if an I/O error occurs
     */
    public static IdGenerator getInstance() throws IOException {
        return instance == null ? instance = new IdGenerator() : instance;
    }

    /**
     * Initializes the current ID from the file.
     */
    private void initialize() {
        try {
            String lastIdStr = fileWorker.read();
            currentId = lastIdStr != null ? Integer.parseInt(lastIdStr) : 0;
        } catch (NumberFormatException e) {
            currentId = 0;
            fileWorker.write(Long.toString(currentId));
        }
    }

    /**
     * Generates a new ID and saves it to the file.
     * @return the newly generated ID
     * @throws IOException if an I/O error occurs
     */
    public synchronized int generateId() throws IOException {
        currentId++;
        fileWorker.write(Long.toString(currentId) + System.lineSeparator());
        return currentId;
    }

    /**
     * Closes the file worker.
     * @throws Exception if an I/O error occurs
     */
    public void close() throws Exception {
        fileWorker.close();
    }
}
