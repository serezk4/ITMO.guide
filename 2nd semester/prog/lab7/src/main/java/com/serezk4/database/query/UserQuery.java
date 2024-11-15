package com.serezk4.database.query;

import com.serezk4.database.model.User;

/**
 * Enumeration of SQL queries related to {@link User} operations.
 *
 * <p>The {@code UserQuery} enum defines a collection of pre-defined SQL queries for interacting
 * with the {@code users} table in the database. Each constant represents a specific query and its
 * corresponding SQL string.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * String sql = UserQuery.FIND_BY_ID.sql();
 * </pre>
 *
 * <p>This enum centralizes query management, making it easier to reuse and maintain SQL statements
 * across the application.</p>
 *
 * <p>Queries include:</p>
 * <ul>
 *     <li>{@link #SAVE_USER}: Saves a new {@link User} record.</li>
 *     <li>{@link #EXISTS_BY_USERNAME}: Checks if a {@link User} exists by username.</li>
 *     <li>{@link #FIND_BY_ID}: Finds a {@link User} by their unique ID.</li>
 *     <li>{@link #FIND_BY_USERNAME}: Finds a {@link User} by their username.</li>
 *     <li>{@link #FIND_ALL}: Retrieves all {@link User} records.</li>
 * </ul>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public enum UserQuery {

    /**
     * SQL query to save a new {@link User} record.
     */
    SAVE_USER("INSERT INTO users (username, password) VALUES (?, ?)"),

    /**
     * SQL query to check if a {@link User} exists by their {@link User#getUsername()}.
     */
    EXISTS_BY_USERNAME("SELECT COUNT(*) FROM users WHERE username = ?"),

    /**
     * SQL query to find a {@link User} by their {@link User#getId()}.
     */
    FIND_BY_ID("SELECT * FROM users WHERE id = ?"),

    /**
     * SQL query to find a {@link User} by their {@link User#getUsername()}.
     */
    FIND_BY_USERNAME("SELECT * FROM users WHERE username = ?"),

    /**
     * SQL query to retrieve all {@link User} records.
     */
    FIND_ALL("SELECT * FROM users");

    /**
     * The SQL query string associated with this enumeration constant.
     */
    private final String sql;

    /**
     * Constructs a {@code UserQuery} enumeration constant with the specified SQL query.
     *
     * @param sql the SQL query string.
     */
    UserQuery(String sql) {
        this.sql = sql;
    }

    /**
     * Retrieves the SQL query string associated with this enumeration constant.
     *
     * @return the SQL query string.
     */
    public String sql() {
        return sql;
    }
}
