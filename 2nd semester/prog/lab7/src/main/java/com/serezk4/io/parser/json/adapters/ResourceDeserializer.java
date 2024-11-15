package com.serezk4.io.parser.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.serezk4.database.model.Person;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom deserializer for a {@link List} of {@link Person} objects.
 * <p>
 * This class implements {@link JsonDeserializer} to handle the deserialization of JSON arrays
 * into {@link List} collections of {@link Person} objects.
 * </p>
 *
 * @see JsonDeserializer
 * @see Person
 * @since 1.0
 */
public class ResourceDeserializer implements JsonDeserializer<List<Person>> {

    /**
     * Deserializes a JSON element into a {@link List} of {@link Person} objects.
     * <p>
     * This method expects the input JSON element to be an array, where each element
     * represents a {@link Person}. Each JSON object in the array is converted to a
     * {@link Person} instance using the provided {@link JsonDeserializationContext}.
     * </p>
     *
     * @param json    the JSON element to deserialize
     * @param typeOfT the type of the object to deserialize to
     * @param context the context for deserialization
     * @return a {@link List} of {@link Person} objects
     * @throws JsonParseException if the JSON is not in the expected format
     */
    @Override
    public List<Person> deserialize(
            final JsonElement json,
            final Type typeOfT,
            final JsonDeserializationContext context
    ) throws JsonParseException {
        List<Person> myCollection = new LinkedList<>();
        json.getAsJsonArray().forEach(element -> myCollection.add(context.deserialize(element, Person.class)));
        return myCollection;
    }
}
