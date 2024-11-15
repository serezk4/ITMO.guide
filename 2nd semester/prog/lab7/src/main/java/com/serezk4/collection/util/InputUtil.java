package com.serezk4.collection.util;

import com.serezk4.database.model.*;
import com.serezk4.io.IOWorker;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility class for handling input operations.
 * <p>
 * The {@code InputUtil} class provides methods for reading user input and parsing
 * it into structured data, such as a {@link Person} object. It supports flexible
 * input validation and exception handling.
 * </p>
 *
 * @author serezk4
 * @see IOWorker
 * @since 1.0
 */
public class InputUtil {

    /**
     * Reads and creates a {@link Person} object from input.
     * <p>
     * This method guides the user through input prompts for each field of the
     * {@code Person} object, including coordinates and location. It validates
     * the input and retries until valid data is provided.
     * </p>
     *
     * @param io the {@link IOWorker} used for reading and writing input
     * @return a {@link Person} object populated with user input
     * @throws InterruptedException if the input process is interrupted
     */
    public static Person get(IOWorker<String> io) throws InterruptedException {
        Person person = new Person();

        while (!input("name", person::setName, Function.identity(), io)) ;
        while (!input("height", person::setHeight, Integer::valueOf, io)) ;
        while (!input("weight", person::setWeight, Integer::valueOf, io)) ;
        while (!input("hair color %s".formatted(
                Arrays.toString(Color.values())), person::setHairColor, Color::valueOf, io)
        ) ;
        while (!input("nationality %s".formatted(
                Arrays.toString(Country.values())), person::setNationality, Country::valueOf, io)
        ) ;

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
     * Reads and parses input for a specific field.
     * <p>
     * This method displays a prompt to the user, validates and parses the input,
     * and updates the specified field using the provided setter method.
     * </p>
     *
     * @param fieldName the name of the field being input
     * @param setter    a method reference to set the field value
     * @param parser    a method reference to parse the input string
     * @param io        the {@link IOWorker} used for reading and writing input
     * @param <K>       the type of the field being set
     * @return {@code true} if the input was successfully processed, {@code false} otherwise
     * @throws InterruptedException if the input process is interrupted
     */
    private static <K> boolean input(
            final String fieldName,
            final Consumer<K> setter,
            final Function<String, K> parser,
            final IOWorker<String> io
    ) throws InterruptedException {
        try {
            String line = io.read(" - " + fieldName);
            if (line == null || line.equals("return")) throw new InterruptedException("called return");

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
