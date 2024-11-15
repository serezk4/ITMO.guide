package com.serezk4.database.service;

import com.serezk4.database.ConnectionManager;
import com.serezk4.database.model.*;
import com.serezk4.database.query.PersonQuery;
import com.serezk4.database.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * {@link Person} service.
 * Implements {@link PersonRepository}.
 */
public class PersonService implements PersonRepository {
    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private static PersonService instance;

    /**
     * Base constructor.
     * Prevents the instantiation of the {@link PersonService}.
     * Use {@link #getInstance()} to get the instance of the {@link PersonService}.
     */
    private PersonService() {
    }

    /**
     * Returns the instance of the PersonService.
     *
     * @return the instance of the {@link PersonService}.
     */
    public static PersonService getInstance() {
        return instance == null ? instance = new PersonService() : instance;
    }

    /**
     * Finds all persons.
     * @return {@code List<Person>} the list of all persons.
     */
    @Override
    public List<Person> findAll() {
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(PersonQuery.FIND_ALL.sql())) {
            try (ResultSet result = query.executeQuery()) {
                List<Person> persons = new ArrayList<>();
                while (result.next()) map(result).ifPresent(persons::add);
                return persons;
            }
        } catch (SQLException e) {
            log.error("Failed to find all persons", e);
            return Collections.emptyList();
        }
    }

    /**
     * Saves a {@link Person}.
     * @param person the {@link Person} to save.
     * @return {@code Optional<Person>} the saved {@link Person}.
     */
    @Override
    public Optional<Person> save(Person person) {
        try(PreparedStatement query = ConnectionManager.getInstance().prepare(PersonQuery.SAVE_PERSON.sql())) {
            query.setLong(1, person.getOwnerId());
            query.setString(2, person.getName());
            query.setInt(3, person.getCoordinates().getX());
            query.setInt(4, person.getCoordinates().getY());
            query.setInt(5, person.getHeight());
            query.setInt(6, person.getWeight());
            query.setString(7, person.getHairColor().name());
            query.setString(8, person.getNationality().name());
            query.setFloat(9, person.getLocation().getX());
            query.setFloat(10, person.getLocation().getY().floatValue());
            query.setString(11, person.getLocation().getName());

            try (ResultSet generatedKeys = query.executeQuery()) {
                if (generatedKeys.next()) person.setId(generatedKeys.getInt(1));
                else throw new SQLException("Creating person failed, no ID obtained.");
            }

            log.info("Person saved: {}", person);
            return Optional.of(person);
        } catch (SQLException e) {
            log.error("Failed to save person", e);
            return Optional.empty();
        }
    }

    /**
     * Removes a {@link Person} by id.
     * @param id the id of the {@link Person} to remove.
     */
    @Override
    public void removeById(int id) {
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(PersonQuery.REMOVE_BY_ID.sql())) {
            query.setInt(1, id);
            query.executeUpdate();
            log.info("Person removed by id: {}", id);
        } catch (SQLException e) {
            log.error("Failed to remove person by id", e);
        }
    }

    /**
     * Maps the {@link ResultSet} to the {@link Person}.
     * @param result the {@link ResultSet} to map.
     * @return the {@link Person} mapped from the {@link ResultSet}.
     * @throws SQLException if an error occurred during the mapping.
     */
    private Optional<Person> map(ResultSet result) throws SQLException {
        if (result == null) return Optional.empty();
        return result.next() ?
                Optional.of(new Person(
                        result.getInt("id"),
                        result.getLong("owner_id"),
                        result.getString("name"),
                        new Coordinates(
                                result.getInt("cord_x"),
                                result.getInt("cord_y")
                        ),
                        new Date(result.getTimestamp("creation_date").toInstant().toEpochMilli()),
                        result.getInt("height"),
                        result.getInt("weight"),
                        Color.valueOf(result.getString("color")),
                        Country.valueOf(result.getString("country")),
                        new Location(
                                result.getFloat("location_x"),
                                (double) result.getFloat("location_y"),
                                result.getString("location_name")
                        )))
                : Optional.empty();
    }
}
