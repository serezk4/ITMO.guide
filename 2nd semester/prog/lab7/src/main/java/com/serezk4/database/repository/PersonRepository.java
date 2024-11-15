package com.serezk4.database.repository;

import com.serezk4.database.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Person} entities.
 *
 * <p>The {@code PersonRepository} interface defines methods for interacting with the
 * {@code persons} table in the database. Implementations of this interface should handle
 * the underlying data access logic.</p>
 *
 * <p>Methods include:</p>
 * <ul>
 *     <li>{@link #findAll()}: Retrieves all {@link Person} records.</li>
 *     <li>{@link #save(Person)}: Saves a {@link Person} record and returns the saved entity.</li>
 *     <li>{@link #removeById(int)}: Deletes a {@link Person} by their unique ID.</li>
 * </ul>
 *
 * <p>Usage Example:</p>
 * <pre>
 * PersonRepository repository = new PersonRepositoryImpl();
 * List&lt;Person&gt; persons = repository.findAll();
 * Optional&lt;Person&gt; savedPerson = repository.save(new Person(...));
 * repository.removeById(1);
 * </pre>
 *
 * <p>This interface can be implemented using various data access technologies, such as JDBC or JPA.</p>
 *
 * <p>Thread Safety:</p>
 * <p>Implementations must ensure thread safety if accessed concurrently.</p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public interface PersonRepository {

    /**
     * Retrieves all {@link Person} records from the database.
     *
     * @return a {@link List} of all {@link Person} entities in the database.
     */
    List<Person> findAll();

    /**
     * Saves a {@link Person} entity to the database.
     *
     * <p>If the save operation is successful, the saved {@link Person} is returned in an
     * {@link Optional}. If the save operation fails, an empty {@link Optional} is returned.</p>
     *
     * @param person the {@link Person} entity to be saved.
     * @return an {@link Optional} containing the saved {@link Person}, or an empty {@link Optional} if the operation fails.
     */
    Optional<Person> save(Person person);

    /**
     * Deletes a {@link Person} entity by their unique ID.
     *
     * <p>If no entity with the given ID exists, the operation should be a no-op.</p>
     *
     * @param id the unique identifier of the {@link Person} to be removed.
     */
    void removeById(int id);
}
