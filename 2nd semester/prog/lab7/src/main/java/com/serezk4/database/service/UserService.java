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
 * {@link User} service.
 * Implements {@link UserRepository}.
 */
public class UserService implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static UserService instance;

    /**
     * Base constructor.
     * Prevents the instantiation of the {@link UserService}.
     * Use {@link #getInstance()} to get the instance of the {@link UserService}.
     */
    private UserService() {}

    /**
     * Returns the instance of the UserService.
     * @return the instance of the {@link UserService}.
     */
    public static UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }

    /**
     * Finds all {@link User}s.
     * @return the list of users. If an error occurred, returns an empty list.
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
     * Saves a user.
     *
     * @param username the username to save.
     * @param password the password to save.
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
     * Checks if a user exists by username.
     *
     * @param username the username to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
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
     * Finds a user by id.
     *
     * @param id the id to find the user by.
     * @return the user.
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
     * Finds a user by username.
     *
     * @param username the username to find the user by.
     * @return the user.
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
     * Checks if the password is correct.
     *
     * @param user     the user to check the password for.
     * @param password the password to check.
     * @return {@code true} if the password is correct, {@code false} otherwise.
     */
    @Override
    public boolean checkPassword(User user, String password) {
        if (user == null || password == null) return false;
        return hashPassword(password).equals(user.getPassword());
    }

    /**
     * Maps the result to the {@link User}.
     *
     * @param result the result to map.
     * @return the {@link User}.
     * @throws SQLException if an error occurred while mapping the result.
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
