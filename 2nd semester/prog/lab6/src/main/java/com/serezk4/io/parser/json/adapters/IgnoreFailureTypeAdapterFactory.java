package com.serezk4.io.parser.json.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A custom {@link TypeAdapterFactory} that skips malformed JSON elements during deserialization.
 * <p>
 * This factory creates {@link TypeAdapter} instances that handle serialization and deserialization
 * while ignoring errors during the reading process. If an error occurs while reading an element,
 * it is skipped, and {@code null} is returned for that element.
 * </p>
 *
 * @see TypeAdapterFactory
 * @see TypeAdapter
 * @since 1.0
 */
public class IgnoreFailureTypeAdapterFactory implements TypeAdapterFactory {

    /**
     * Creates a {@link TypeAdapter} for the given type, wrapping it to handle errors gracefully.
     *
     * @param gson the {@link Gson} instance
     * @param type the type for which the adapter is being created
     * @param <T>  the type of objects being handled
     * @return a custom {@link TypeAdapter} for the given type
     */
    @Override
    public final <T> TypeAdapter<T> create(
            final Gson gson,
            final TypeToken<T> type
    ) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return createCustomTypeAdapter(delegate);
    }

    /**
     * Wraps the given {@link TypeAdapter} to skip malformed JSON elements during deserialization.
     *
     * @param delegate the original {@link TypeAdapter} to be wrapped
     * @param <T>      the type of objects being handled
     * @return a custom {@link TypeAdapter} that handles deserialization failures gracefully
     */
    private <T> TypeAdapter<T> createCustomTypeAdapter(
            final TypeAdapter<T> delegate
    ) {
        return new TypeAdapter<>() {
            @Override
            public void write(final JsonWriter out, final T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(final JsonReader in) throws IOException {
                try {
                    return delegate.read(in);
                } catch (Exception e) {
                    in.skipValue();
                    System.out.println(e.getMessage());
                    return null;
                }
            }
        };
    }
}
