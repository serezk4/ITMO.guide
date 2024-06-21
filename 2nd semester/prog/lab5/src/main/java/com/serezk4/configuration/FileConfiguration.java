package com.serezk4.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * FileConfiguration class.
 */
public class FileConfiguration {
    public static final Path ID_FILE_PATH = Path.of("ID_SEQ");
    public static final Path DATA_FILE_PATH = Path.of(Optional.ofNullable(System.getenv("DATA_FILE_PATH")).orElse("data.json"));

    static {
        checkFile(ID_FILE_PATH);
        checkFile(DATA_FILE_PATH);
    }

    private static void checkFile(Path path) {
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
