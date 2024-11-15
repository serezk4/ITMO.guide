package com.serezk4.database.service;

import com.serezk4.database.ConnectionManager;
import com.serezk4.database.model.User;
import com.serezk4.database.query.UserQuery;
import com.serezk4.database.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.serezk4.security.PasswordUtil.hashPassword;

/**
 * Service class for managing {@link User} entities.
 *
 * <p>The {@code UserService} class implements the {@link UserRepository} interface,
 * providing concrete methods for CRUD operations on {@link User} objects. It handles
 * database interactions for the {@code users} table, including user retrieval, creation,
 * and password validation.</p>
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *     <li>Executes database queries for retrieving, saving, and validating {@link User} records.</li>
 *     <li>Ensures data consistency and handles error logging during database operations.</li>
 *     <li>Maps database result sets into {@link User} objects for use in business logic.</li>
 * </ul>
 *
 * <p>Design Pattern:</p>
 * <p>Implements the Singleton design pattern to ensure only one instance of the service
 * exists during the application’s lifecycle. Use {@link #getInstance()} to access the singleton instance.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * UserService userService = UserService.getInstance();
 * List<User> users = userService.findAll();
 * Optional<User> user = userService.findByUsername("username");
 * userService.save("username", "password");
 * boolean exists = userService.existsByUsername("username");
 * </pre>
 *
 * @author serezk4
 * @version 1.0
 * @see User
 * @see UserRepository
 * @see UserQuery
 * @since 1.0
 */
public final class UserService implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static UserService instance;

    /**
     * Constructs the {@link UserService}.
     *
     * <p>This private constructor enforces the Singleton pattern, ensuring that only one
     * instance of the {@link UserService} exists during the application's lifecycle.
     * Direct instantiation is prevented to centralize data access logic.</p>
     *
     * <p>Use {@link #getInstance()} to obtain the singleton instance.</p>
     *
     * <p>Design considerations:</p>
     * <ul>
     *     <li>Encapsulates all {@link User} database operations in a single service.</li>
     *     <li>Reduces redundancy and centralizes database access logic.</li>
     * </ul>
     *
     * @see #getInstance()
     * @see UserRepository
     */
    private UserService() {
    }

    /**
     * Provides the singleton instance of the {@link UserService}.
     *
     * <p>If the instance is {@code null}, a new {@link UserService} is created. Subsequent
     * calls to this method will return the same instance. This ensures consistent access to
     * shared resources, such as database connections.</p>
     *
     * <p>Usage Example:</p>
     * <pre>
     * UserService userService = UserService.getInstance();
     * </pre>
     *
     * <p>Thread Safety:</p>
     * <p>This method is not inherently thread-safe. Ensure external synchronization if used
     * in a multi-threaded environment.</p>
     *
     * @return the singleton instance of the {@link UserService}.
     * @throws RuntimeException if initialization fails due to unexpected errors.
     * @see UserService
     * @see UserRepository
     */
    public static UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }

    /**
     * Retrieves all {@link User} records from the database.
     *
     * <p>This method executes the {@link UserQuery#FIND_ALL} SQL query to fetch all rows
     * from the {@code users} table. Each row is mapped to a {@link User} object using the
     * {@link #map(ResultSet)} method. If an SQL error occurs during execution, an empty
     * list is returned, and the error is logged.</p>
     *
     * <p>Performance Considerations:</p>
     * <ul>
     *     <li>Large datasets may impact performance; consider using pagination for scalable solutions.</li>
     *     <li>Efficiently processes {@link ResultSet} to reduce memory overhead.</li>
     * </ul>
     *
     * @return a {@link List} of all {@link User} entities, or an empty list if none are found
     * or if an error occurs.
     * @see User
     * @see UserQuery#FIND_ALL
     * @see #map(ResultSet)
     */
    @Override
    public List<User> findAll() {
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(UserQuery.FIND_ALL.sql())) {
            try (ResultSet result = query.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (result.next()) map(result).ifPresent(users::add);
                return users;
            }
        } catch (SQLException e) {
            log.error("Failed to find all users", e);
            return Collections.emptyList();
        }
    }

    /**
     * Saves a new {@link User} to the database.
     *
     * <p>This method hashes the password before saving it using
     * {@link com.serezk4.security.PasswordUtil#hashPassword(String)}. It executes the
     * {@link UserQuery#SAVE_USER} SQL query to insert the user into the {@code users} table.</p>
     *
     * @param username the username to save.
     * @param password the password to save.
     * @see User
     * @see UserQuery#SAVE_USER
     */
    @Override
    public void save(String username, String password) {
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(UserQuery.SAVE_USER.sql())) {
            query.setString(1, username);
            query.setString(2, hashPassword(password));

            query.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to save user", e);
        }
    }

    /**
     * Checks if a {@link User} exists in the database by their username.
     *
     * <p>Executes the {@link UserQuery#EXISTS_BY_USERNAME} SQL query to count records
     * with the specified username in the {@code users} table.</p>
     *
     * @param username the username to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     * @see UserQuery#EXISTS_BY_USERNAME
     */
    @Override
    public boolean existsByUsername(String username) {
        if (username == null) return false;
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(UserQuery.EXISTS_BY_USERNAME.sql())) {
            query.setString(1, username);

            try (ResultSet result = query.executeQuery()) {
                return result.next() && result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            log.error("Failed to check if user exists by username", e);
            return false;
        }
    }

    /**
     * Finds a {@link User} by their unique ID.
     *
     * <p>Executes the {@link UserQuery#FIND_BY_ID} SQL query to retrieve the user with
     * the specified ID from the {@code users} table.</p>
     *
     * @param id the unique identifier of the user to find.
     * @return an {@link Optional} containing the {@link User} if found, or an empty
     * {@link Optional} if not found.
     * @see UserQuery#FIND_BY_ID
     */
    @Override
    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(UserQuery.FIND_BY_ID.sql())) {
            query.setLong(1, id);

            try (ResultSet result = query.executeQuery()) {
                return map(result);
            }
        } catch (SQLException e) {
            log.error("Failed to find user by id", e);
            return Optional.empty();
        }
    }

    /**
     * Finds a {@link User} by their username.
     *
     * <p>Executes the {@link UserQuery#FIND_BY_USERNAME} SQL query to retrieve the user
     * with the specified username from the {@code users} table.</p>
     *
     * @param username the username of the user to find.
     * @return an {@link Optional} containing the {@link User} if found, or an empty
     * {@link Optional} if not found.
     * @see UserQuery#FIND_BY_USERNAME
     */
    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        try (PreparedStatement query = ConnectionManager.getInstance().prepare(UserQuery.FIND_BY_USERNAME.sql())) {
            query.setString(1, username);

            try (ResultSet result = query.executeQuery()) {
                return map(result);
            }
        } catch (SQLException e) {
            log.error("Failed to find user by username", e);
            return Optional.empty();
        }
    }

    /**
     * Verifies if the provided password matches the hashed password of the given {@link User}.
     *
     * <p>The password is hashed using {@link com.serezk4.security.PasswordUtil#hashPassword(String)}
     * and compared to the stored hash.</p>
     *
     * @param user     the user whose password is to be verified.
     * @param password the plain-text password to verify.
     * @return {@code true} if the password matches, {@code false} otherwise.
     */
    @Override
    public boolean checkPassword(User user, String password) {
        if (user == null || password == null) return false;
        return hashPassword(password).equals(user.getPassword());
    }

    /**
     * Maps a row in the {@link ResultSet} to a {@link User} entity.
     *
     * <p>This method extracts data from the {@link ResultSet} and creates a new {@link User} object
     * with its fields populated from the database columns.</p>
     *
     * @param result the {@link ResultSet} containing user data.
     * @return an {@link Optional} containing the mapped {@link User}, or an empty {@link Optional}
     * if the result set is empty.
     * @throws SQLException if an error occurs while accessing the result set.
     * @see User
     * @see ResultSet
     */
    private Optional<User> map(ResultSet result) throws SQLException {
        if (result == null) return Optional.empty();
        return result.next() ?
                Optional.of(new User(
                        result.getLong("id"),
                        result.getString("username"),
                        result.getString("password")))
                : Optional.empty();
    }
}
