package com.serezk4.command;

import com.serezk4.io.file.BufferedFileWorker;
import com.serezk4.io.file.FileWorker;
import com.serezk4.io.trasnfer.Request;
import com.serezk4.io.trasnfer.Response;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Command to execute script from file
 */
public class ExecuteScript extends Command {
    protected ExecuteScript() {
        super("exec", new String[]{"filepath"},"execute script");
    }

    /**
     * Executes script from file
     * @param request request to execute
     * @return response with script
     * @see Response#script
     * <p>
     * @check if file exists
     * @check if file is a file
     * @check if file can be read
     */
    @Override
    public Response execute(Request request) {
        if (request.args() == null || request.args().isEmpty()) return new Response("No file path provided.");

        Path path = Paths.get(request.args().get(0));
        if (!path.toFile().exists()) return new Response("File not found.");
        if (!path.toFile().isFile()) return new Response("Path is not a file.");
        if (!path.toFile().canRead()) return new Response("Not enough rights to read file.");

        try(FileWorker file = new BufferedFileWorker(path)) {
            return new Response("Script loaded from file %s".formatted(path.toString()), Collections.emptyList(), file.read());
        } catch (Exception e) {
            return new Response("error caught: %s".formatted(e.getMessage()));
        }
    }
}
