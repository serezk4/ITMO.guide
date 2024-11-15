package com.serezk4.io.parser;

import com.serezk4.io.IOWorker;

import java.util.List;

/**
 * Interface for formatting data.
 *
 * @param <T> transfer data type
 */
public interface Formatter<T> extends IOWorker<List<T>> {
}
