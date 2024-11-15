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
 * Service class for managing {@link Person} entities.
 *
 * <p>The {@code PersonService} class implements the {@link PersonRepository} interface,
 * providing concrete methods for performing CRUD operations on {@link Person} objects.
 * It acts as a bridge between the database and the application, encapsulating the logic
 * required to interact with the {@code persons} table.</p>
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *     <li>Handles database queries for retrieving, saving, and deleting {@link Person} records.</li>
 *     <li>Ensures consistency and error handling during database operations.</li>
 *     <li>Maps database results into {@link Person} objects.</li>
 * </ul>
 *
 * <p>Design Pattern:</p>
 * <p>Implements the Singleton design pattern to ensure only one instance of the service
 * exists during the application’s lifecycle. Use {@link #getInstance()} to access the singleton instance.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * PersonService personService = PersonService.getInstance();
 * List&lt;Person&gt; persons = personService.findAll();
 * Optional&lt;Person&gt; savedPerson = personService.save(new Person(...));
 * personService.removeById(1);
 * </pre>
 *
 * @author serezk4
 * @version 1.0
 * @see Person
 * @see PersonRepository
 * @see #getInstance()
 * @since 1.0
 */
public class PersonService implements PersonRepository {
    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private static PersonService instance;

    /**
     * Constructs the {@link PersonService}.
     *
     * <p>This private constructor enforces the Singleton pattern, ensuring that only one
     * instance of the {@link PersonService} exists during the application's lifecycle.
     * Direct instantiation is prevented to centralize data access logic.</p>
     *
     * <p>Use {@link #getInstance()} to obtain the singleton instance.</p>
     *
     * <p>Design considerations:</p>
     * <ul>
     *     <li>Encapsulates all {@link Person} database operations in a single service.</li>
     *     <li>Reduces redundancy and centralizes database access logic.</li>
     * </ul>
     *
     * @see #getInstance()
     * @see PersonRepository
     */
    private PersonService() {
    }

    /**
     * Provides the singleton instance of the {@link PersonService}.
     *
     * <p>If the instance is {@code null}, a new {@link PersonService} is created. Subsequent
     * calls to this method will return the same instance. This ensures consistent access to
     * shared resources, such as database connections.</p>
     *
     * <p>Usage Example:</p>
     * <pre>
     * PersonService personService = PersonService.getInstance();
     * </pre>
     *
     * <p>Thread Safety:</p>
     * <p>This method is not inherently thread-safe. Ensure external synchronization if used
     * in a multi-threaded environment.</p>
     *
     * @return the singleton instance of the {@link PersonService}.
     * @throws RuntimeException if initialization fails due to unexpected errors.
     * @see PersonService
     * @see PersonRepository
     */
    public static PersonService getInstance() {
        return instance == null ? instance = new PersonService() : instance;
    }

    /**
     * Retrieves all {@link Person} records from the database.
     *
     * <p>This method executes the {@link PersonQuery#FIND_ALL} SQL query to fetch all
     * rows from the {@code persons} table. Each row is mapped to a {@link Person} object
     * using the {@link #map(ResultSet)} method. If an SQL error occurs during execution,
     * an empty list is returned, and the error is logged.</p>
     *
     * <p>Thread Safety:</p>
     * <p>This method uses a single database connection and is not thread-safe. External
     * synchronization is required for concurrent access.</p>
     *
     * <p>Performance Considerations:</p>
     * <ul>
     *     <li>Large datasets may impact performance; consider using pagination for scalable solutions.</li>
     *     <li>Efficiently processes {@link ResultSet} to reduce memory overhead.</li>
     * </ul>
     *
     * @return a {@link List} of all {@link Person} entities, or an empty list if none are found
     * or if an error occurs.
     * @throws RuntimeException if mapping errors occur during result processing.
     * @usage Fetch all database entries for reporting or analytics.
     * @see Person
     * @see PersonQuery#FIND_ALL
     * @see #map(ResultSet)
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
     * Saves a {@link Person} entity to the database.
     *
     * <p>This method executes the {@link PersonQuery#SAVE_PERSON} SQL query to insert a new
     * {@link Person} into the {@code persons} table. All attributes of the {@link Person} are
     * set as parameters in the query. Upon successful insertion, the database-generated ID
     * is retrieved and assigned to the {@link Person} object.</p>
     *
     * <p>Thread Safety:</p>
     * <p>This method is not thread-safe and assumes single-threaded usage. For concurrent
     * applications, external synchronization is recommended.</p>
     *
     * <p>Failure Handling:</p>
     * <ul>
     *     <li>Returns an empty {@link Optional} if the save operation fails.</li>
     *     <li>Logs detailed error messages for debugging and monitoring.</li>
     * </ul>
     *
     * @param person the {@link Person} entity to save.
     * @return an {@link Optional} containing the saved {@link Person}, or an empty
     * {@link Optional} if the operation fails.
     * @usage Save user data or records as part of business operations.
     * @performance Optimized for single inserts; bulk inserts may require a different implementation.
     * @see Person
     * @see PersonQuery#SAVE_PERSON
     */
    @Override
    public Optional<Person> save(Person person) {
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(PersonQuery.SAVE_PERSON.sql())) {
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
     * Deletes a {@link Person} entity from the database by its unique ID.
     *
     * <p>This method executes the {@link PersonQuery#REMOVE_BY_ID} SQL query to remove
     * a record from the {@code persons} table. If the specified ID does not exist, no action
     * is performed.</p>
     *
     * <p>Failure Handling:</p>
     * <ul>
     *     <li>Logs detailed error messages if the operation fails due to SQL exceptions.</li>
     * </ul>
     *
     * @param id the unique identifier of the {@link Person} to delete.
     * @usage Remove expired, unused, or invalid user records.
     * @performance Optimized for single deletions; bulk deletions may require a different strategy.
     * @see Person
     * @see PersonQuery#REMOVE_BY_ID
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
     * Maps a {@link ResultSet} row to a {@link Person} entity.
     *
     * <p>This method extracts data from the specified {@link ResultSet} and creates a
     * {@link Person} object. Complex fields such as {@link Coordinates}, {@link Location},
     * and enumerations like {@link Color} and {@link Country} are also populated.</p>
     *
     * <p>Error Handling:</p>
     * <ul>
     *     <li>Returns an empty {@link Optional} if the {@link ResultSet} is empty.</li>
     *     <li>Throws a {@link SQLException} if errors occur while reading data.</li>
     * </ul>
     *
     * @param result the {@link ResultSet} containing data to map.
     * @return an {@link Optional} containing the mapped {@link Person}, or an empty
     * {@link Optional} if the {@link ResultSet} is empty.
     * @throws SQLException if an error occurs during data extraction.
     * @usage Converts database query results into usable domain objects.
     * @performance Efficient mapping; uses minimal memory for large datasets.
     * @limitations Requires valid and non-corrupt database schema.
     * @design Separates mapping logic for reusability and readability.
     * @see Person
     * @see ResultSet
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
