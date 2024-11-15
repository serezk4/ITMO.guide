package com.serezk4.database.repository;

import com.serezk4.database.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 *
 * <p>The {@code UserRepository} interface defines methods for interacting with the
 * {@code users} table in the database. Implementations of this interface handle data access
 * logic for {@link User} entities, such as finding, saving, and verifying users.</p>
 *
 * <p>Methods include:</p>
 * <ul>
 *     <li>{@link #findAll()}: Retrieves all {@link User} records.</li>
 *     <li>{@link #save(String, String)}: Saves a new {@link User} record with a username and password.</li>
 *     <li>{@link #existsByUsername(String)}: Checks if a {@link User} exists by their username.</li>
 *     <li>{@link #findById(Long)}: Finds a {@link User} by their unique ID.</li>
 *     <li>{@link #findByUsername(String)}: Finds a {@link User} by their username.</li>
 *     <li>{@link #checkPassword(User, String)}: Verifies a {@link User}'s password.</li>
 * </ul>
 *
 * <p>Usage Example:</p>
 * <pre>
 * UserRepository repository = new UserRepositoryImpl();
 * repository.save("username", "password");
 * Optional&lt;User&gt; user = repository.findByUsername("username");
 * boolean exists = repository.existsByUsername("username");
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
public interface UserRepository {

    /**
     * Retrieves all {@link User} records from the database.
     *
     * @return a {@link List} of all {@link User} entities in the database.
     */
    List<User> findAll();

    /**
     * Saves a new {@link User} to the database with the specified username and password.
     *
     * @param username the username of the new {@link User}.
     * @param password the password of the new {@link User}.
     */
    void save(String username, String password);

    /**
     * Checks if a {@link User} exists in the database by their username.
     *
     * @param username the username to check for existence.
     * @return {@code true} if a user with the specified username exists; {@code false} otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Finds a {@link User} by their unique ID.
     *
     * @param id the unique identifier of the {@link User}.
     * @return an {@link Optional} containing the {@link User} if found, or an empty {@link Optional} if not found.
     */
    Optional<User> findById(Long id);

    /**
     * Finds a {@link User} by their username.
     *
     * @param username the username of the {@link User}.
     * @return an {@link Optional} containing the {@link User} if found, or an empty {@link Optional} if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifies if the specified password matches the password of the given {@link User}.
     *
     * @param user     the {@link User} whose password is to be verified.
     * @param password the password to verify.
     * @return {@code true} if the password matches; {@code false} otherwise.
     */
    boolean checkPassword(User user, String password);
}
