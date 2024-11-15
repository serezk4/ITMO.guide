package com.serezk4.io.file;

import com.serezk4.io.IOWorker;

/**
 * Interface for reading and writing data from/to the file.
 */

public interface FileWorker extends IOWorker<String> {
    /**
     * Writes data to the file.
     *
     * @return data in String format
     */
    default String readAll() {
        StringBuilder text = new StringBuilder();
        while (ready()) text.append(read()).append(System.lineSeparator());
        return text.toString();
    }
}
