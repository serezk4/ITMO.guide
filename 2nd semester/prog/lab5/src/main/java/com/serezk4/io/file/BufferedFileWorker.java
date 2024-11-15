package com.serezk4.io.file;

import java.io.*;
import java.nio.file.Path;

/**
 * Buffered implementation of the {@link FileWorker} interface.
 * <p>
 * This class provides efficient file reading and writing using {@link BufferedReader}
 * and {@link BufferedWriter}. It supports both appending and overwriting data in files.
 * </p>
 *
 * @see FileWorker
 * @see BufferedReader
 * @see BufferedWriter
 * @since 1.0
 */
public final class BufferedFileWorker implements FileWorker {

    private final BufferedReader fileReader;
    private final BufferedWriter fileWriter;

    /**
     * Creates a new instance of the {@code BufferedFileWorker} with the specified file path.
     * <p>
     * Allows control over whether data is appended to or overwrites the file.
     * </p>
     *
     * @param path   the path to the file
     * @param append {@code true} to append data to the file, {@code false} to overwrite it
     * @throws IOException if an I/O error occurs while opening the file
     */
    public BufferedFileWorker(
            final Path path,
            final boolean append
    ) throws IOException {
        this.fileReader = new BufferedReader(new FileReader(path.toFile()));
        this.fileWriter = new BufferedWriter(new FileWriter(path.toFile(), append));
    }

    /**
     * Creates a new instance of the {@code BufferedFileWorker} with the specified file path.
     * <p>
     * Data will be appended to the file by default.
     * </p>
     *
     * @param path the path to the file
     * @throws IOException if an I/O error occurs while opening the file
     */
    public BufferedFileWorker(final Path path) throws IOException {
        this(path, true);
    }

    /**
     * Reads a line of text from the file.
     * <p>
     * This method reads the next line from the file. If an I/O error occurs, {@code null}
     * is returned.
     * </p>
     *
     * @return the next line from the file, or {@code null} if an error occurs or the end of the file is reached
     */
    @Override
    public String read() {
        try {
            return fileReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Writes a string of data to the file.
     * <p>
     * The data is appended or overwrites the file based on the configuration provided during
     * initialization. If an I/O error occurs, the exception is silently ignored.
     * </p>
     *
     * @param data the string of data to write
     */
    @Override
    public void write(final String data) {
        try {
            fileWriter.append(data).flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Checks if there is more data to read from the file.
     *
     * @return {@code true} if the file is ready for reading, {@code false} otherwise
     */
    @Override
    public boolean ready() {
        try {
            return fileReader.ready();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Closes the file reader and writer streams.
     * <p>
     * Once closed, no further read or write operations can be performed on this worker.
     * </p>
     *
     * @throws Exception if an I/O error occurs during closure
     */
    @Override
    public void close() throws Exception {
        fileReader.close();
        fileWriter.close();
    }
}
