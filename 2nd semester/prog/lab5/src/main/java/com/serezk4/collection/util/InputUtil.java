package com.serezk4.collection.util;

import com.serezk4.collection.model.*;
import com.serezk4.io.IOWorker;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * InputUtil class.
 */
public class InputUtil {
    /**
     * Get person from input.
     * @param io IOWorker
     * @return Person
     * @throws InterruptedException if interrupted
     */
    public Person get(IOWorker<String> io) throws InterruptedException, IOException {
        Person person = new Person();

        while (!input("name", person::setName, Function.identity(), io)) ;
        while (!input("height", person::setHeight, Integer::valueOf, io)) ;
        while (!input("weight", person::setWeight, Integer::valueOf, io)) ;
        while (!input("hair color %s".formatted(Arrays.toString(Color.values())), person::setHairColor, Color::valueOf, io)) ;
        while (!input("nationality %s".formatted(Arrays.toString(Country.values())), person::setNationality, Country::valueOf, io)) ;

        Coordinates coordinates = new Coordinates();
        while (!input("cord x", coordinates::setX, Integer::valueOf, io)) ;
        while (!input("cord y", coordinates::setY, Integer::valueOf, io)) ;
        person.setCoordinates(coordinates);

        Location location = new Location();
        while (!input("location x", location::setX, Float::valueOf, io)) ;
        while (!input("location y", location::setY, Double::valueOf, io)) ;
        person.setLocation(location);

        return person;
    }

    /**
     * Input method.
     * @param fieldName field name
     * @param setter setter method reference
     * @param parser parser method reference
     * @param io IOWorker
     * @return true if success
     * @param <K> type
     * @throws InterruptedException if interrupted
     */
    private <K> boolean input(String fieldName, Consumer<K> setter, Function<String, K> parser, IOWorker<String> io) throws InterruptedException {
        try {
            String line = io.read(" - " + fieldName);
            if (line == null || line.equals("return")) throw new InterruptedException("вызван return");

            if (line.isBlank()) setter.accept(null);
            else setter.accept(parser.apply(line));

            return true;
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception ex) {
            io.write(ex.getMessage() + System.lineSeparator());
            return false;
        }
    }
}
