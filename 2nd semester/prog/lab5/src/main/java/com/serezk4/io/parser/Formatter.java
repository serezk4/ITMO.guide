package com.serezk4.io.parser;

import com.serezk4.io.IOWorker;

import java.util.List;

/**
 * Interface for formatting and transferring data.
 * <p>
 * The {@code Formatter} interface extends {@link IOWorker} to provide functionality
 * for reading and writing lists of objects of a specific type. It serves as a base
 * for data formatting operations such as parsing and serialization.
 * </p>
 *
 * @param <T> the type of data being transferred and formatted
 * @see IOWorker
 * @since 1.0
 */
public interface Formatter<T> extends IOWorker<List<T>> {
}
