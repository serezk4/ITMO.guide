package com.serezk4.io.parser.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import se.ifmo.collection.MyCollection;
import se.ifmo.collection.objects.City;

import java.lang.reflect.Type;

public class ResourceDeserializer implements JsonDeserializer<MyCollection> {
    @Override
    public MyCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        MyCollection myCollection = new MyCollection();
        json.getAsJsonObject().entrySet().forEach(entry -> {
            long key = Long.parseLong(entry.getKey());
            City city = context.deserialize(entry.getValue(), City.class);
            myCollection.put(key, city);
        });

        return myCollection;
    }
}
