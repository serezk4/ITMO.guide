package com.serezk4.io.parser.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.serezk4.collection.model.Person;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ResourceDeserializer implements JsonDeserializer<List<Person>> {
    @Override
    public List<Person> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        List<Person> myCollection = new LinkedList<>();
        json.getAsJsonArray().forEach(element -> myCollection.add(context.deserialize(element, Person.class)));
        return myCollection;
    }
}
