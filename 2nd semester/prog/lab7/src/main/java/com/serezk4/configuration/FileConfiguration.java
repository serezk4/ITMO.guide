package com.serezk4.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Configuration class for file paths used in the application.
 * <p>
 * The {@code FileConfiguration} class provides constants for file paths used by the application,
 * such as the ID sequence file and the data storage file. It ensures that these files exist
 * and are accessible, creating them if necessary.
 * </p>
 *
 * @since 1.0
 */
public final class FileConfiguration {

    /**
     * Path to the file used for storing the ID sequence.
     */
    public static final Path ID_FILE_PATH = Path.of("ID_SEQ");
    /**
     * Path to the file used for storing application data.
     * <p>
     * The path is determined from the {@code DATA_FILE_PATH} environment variable,
     * defaulting to {@code data.json} if not set.
     * </p>
     */
    public static final Path DATA_FILE_PATH = Path.of(Optional.ofNullable(System.getenv("DATA_FILE_PATH"))
            .orElse("data.json"));

    static {
        checkFile(ID_FILE_PATH);
        checkFile(DATA_FILE_PATH);
    }

    /**
     * Unit-class.
     */
    private FileConfiguration() {
    }

    /**
     * Ensures that the specified file exists and is accessible.
     * <p>
     * If the file does not exist, this method attempts to create it. Warnings are issued
     * if the file is not readable or writable. If file creation fails, the application
     * exits with an error status.
     * </p>
     *
     * @param path the {@link Path} of the file to check
     */
    private static void checkFile(final Path path) {
        if (!path.toFile().exists()) {
            System.err.printf("[ERROR] File %s does not exist%n", path.toAbsolutePath());
            try {
                Files.createFile(path);
                System.out.printf("[INFO] File %s created%n", path.toAbsolutePath());
            } catch (IOException e) {
                System.err.printf("[ERROR] Failed to create file %s%n", path.toAbsolutePath());
                System.exit(1);
            }
        }

        if (!path.toFile().canRead()) System.err.printf("[WARNING] File %s is not readable%n", path.toAbsolutePath());
        if (!path.toFile().canWrite()) System.err.printf("[WARNING] File %s is not writable%n", path.toAbsolutePath());
    }
}
