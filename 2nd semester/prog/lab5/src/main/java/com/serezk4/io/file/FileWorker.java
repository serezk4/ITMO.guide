package com.serezk4.io.file;

import com.serezk4.io.IOWorker;

/**
 * Interface for reading and writing data to and from a file.
 * <p>
 * Extends the {@link IOWorker} interface to provide file-specific functionality,
 * including reading all content from a file.
 * </p>
 *
 * @see IOWorker
 * @since 1.0
 */
public interface FileWorker extends IOWorker<String> {

    /**
     * Reads all data from the file.
     * <p>
     * Continuously reads from the file until no more data is available,
     * appending each line to a {@link StringBuilder}.
     * </p>
     *
     * @return a string containing all the data from the file
     */
    default String readAll() {
        StringBuilder text = new StringBuilder();
        while (ready()) {
            text.append(read()).append(System.lineSeparator());
        }
        return text.toString();
    }
}
