package com.serezk4.io.file;

import java.io.*;
import java.nio.file.Path;

/**
 * Buffered implementation of the FileWorker interface.
 *
 * @see FileWorker
 *
 * for more information >>
 * @see BufferedReader
 * @see BufferedWriter
 */
public class BufferedFileWorker implements FileWorker{
    private final BufferedReader fileReader;
    private final BufferedWriter fileWriter;

    /**
     * Creates a new instance of the BufferedFileWorker class.
     * @param path path to the file
     * @param append true if the data should be appended to the file, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public BufferedFileWorker(Path path, boolean append) throws IOException {
        this.fileReader = new BufferedReader(new FileReader(path.toFile()));
        this.fileWriter = new BufferedWriter(new FileWriter(path.toFile(), append));

        fileReader.reset();
    }

    /**
     * Creates a new instance of the BufferedFileWorker class.
     * The data will be appended to the file.
     * @param path path to the file
     * @throws IOException if an I/O error occurs
     */
    public BufferedFileWorker(Path path) throws IOException {
        this(path, true);
    }


    /**
     * Reads data from the file.
     * @return data in String format
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
     * Writes data to the file.
     * @param data data in String format
     */
    @Override
    public void write(String data) {
        try {
            fileWriter.append(data).flush();
        } catch (IOException ignored) {}
    }

    /**
     * Checks if the worker is ready to read data.
     * @return true if the worker is ready, false otherwise
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
     * Closes the file reader and writer.
     * @throws Exception if an I/O error occurs
     */
    @Override
    public void close() throws Exception {
        fileReader.close();
        fileWriter.close();
    }
}
