package com.serezk4.command;

import com.serezk4.io.file.BufferedFileWorker;
import com.serezk4.io.file.FileWorker;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Command to execute a script from a file.
 * <p>
 * The {@code ExecuteScript} command reads a script from a specified file and returns
 * its content as a script in the {@link Response}. The command performs several checks
 * to ensure the file is valid and readable.
 * </p>
 *
 * @see Response#script
 * @see FileWorker
 * @since 1.0
 */
public final class ExecuteScript extends Command {

    /**
     * Constructs a new {@code ExecuteScript} command.
     * <p>
     * The command is named "exec" and expects a single argument: the file path to the script.
     * </p>
     */
    ExecuteScript() {
        super("exec", new String[]{"filepath"}, "execute script");
    }

    /**
     * Executes the script from the specified file.
     * <p>
     * Validates the file path provided in the {@link Request}. If the file exists, is a regular
     * file, and is readable, the script content is loaded and returned in the {@link Response}.
     * Otherwise, an error message is returned.
     * </p>
     *
     * @param request the request containing the file path as an argument
     * @return a {@link Response} containing the script or an error message
     * @see Response#script
     */
    @Override
    public Response execute(final Request request) {
        if (request.args() == null || request.args().isEmpty()) {
            return new Response("No file path provided.");
        }

        final Path path = Paths.get(request.args().get(0));
        if (!path.toFile().exists()) return new Response("File not found.");
        if (!path.toFile().isFile()) return new Response("Path is not a file.");
        if (!path.toFile().canRead()) return new Response("Not enough rights to read file.");

        try (FileWorker file = new BufferedFileWorker(path)) {
            StringBuilder script = new StringBuilder();
            while (file.ready()) {
                script.append(file.read()).append(System.lineSeparator());
            }
            return new Response(
                    "Script loaded from file %s".formatted(path.toString()),
                    Collections.emptyList(),
                    script.toString()
            );
        } catch (Exception e) {
            return new Response("Error occurred: %s".formatted(e.getMessage()));
        }
    }
}
