package com.serezk4.collection.util;

import com.serezk4.database.model.*;
import com.serezk4.io.IOWorker;

import java.util.List;
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
        Coordinates coordinates = new Coordinates();
        Location location = new Location();

        final List<FieldRequest<?>> fields = List.of(
                new FieldRequest<>("name", person::setName, Function.identity()),
                new FieldRequest<>("height", person::setHeight, Integer::valueOf),
                new FieldRequest<>("weight", person::setWeight, Integer::valueOf),
                new FieldRequest<>("hair %s".formatted(Color.formattedList()), person::setHairColor, Color::valueOf),
                new FieldRequest<>("country %s".formatted(Country.formattedList()), person::setNationality, Country::valueOf),
                new FieldRequest<>("cord x", coordinates::setX, Integer::valueOf),
                new FieldRequest<>("cord y", coordinates::setY, Integer::valueOf),
                new FieldRequest<>("location x", location::setX, Float::valueOf),
                new FieldRequest<>("location y", location::setY, Double::valueOf)
        );

        for (FieldRequest<?> fieldRequest : fields) fieldRequest.get(io);

        person.setLocation(location);
        person.setCoordinates(coordinates);

        return person;
    }

    private record FieldRequest<K>(String name, Consumer<K> setter, Function<String, K> parser) {
        public void get(IOWorker<String> io) throws InterruptedException {
            while (!input(name, setter, parser, io)) ;
        }
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
            if (line == null || line.equals("return") || line.equals("break")) throw new InterruptedException("called return");

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
