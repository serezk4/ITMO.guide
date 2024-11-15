package com.serezk4.database.query;

import com.serezk4.database.model.Person;

/**
 * Enumeration of SQL queries related to {@link Person} operations.
 *
 * <p>The {@code PersonQuery} enum defines a set of pre-defined SQL queries for interacting
 * with the {@code persons} table in the database. Each enumeration constant represents a
 * specific query and its associated SQL string.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * String sql = PersonQuery.FIND_ALL.sql();
 * </pre>
 *
 * <p>This enum simplifies query management by centralizing the SQL definitions, making it easier
 * to reuse and maintain queries across the application.</p>
 *
 * <p>Queries include:</p>
 * <ul>
 *     <li>{@link #FIND_ALL}: Retrieves all {@link Person} records.</li>
 *     <li>{@link #SAVE_PERSON}: Inserts a new {@link Person} and returns the generated ID.</li>
 *     <li>{@link #REMOVE_BY_ID}: Deletes a {@link Person} by their unique ID.</li>
 * </ul>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public enum PersonQuery {

    /**
     * SQL query to retrieve all {@link Person} records.
     */
    FIND_ALL("SELECT * FROM persons"),

    /**
     * SQL query to save a new {@link Person} record and return the generated ID.
     */
    SAVE_PERSON("INSERT INTO persons (owner_id, name, cord_x, cord_y, height, weight, color, country, location_x, location_y, location_name) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id"),

    /**
     * SQL query to delete a {@link Person} by their unique ID.
     */
    REMOVE_BY_ID("DELETE FROM persons WHERE id = ?");

    /**
     * The SQL query string associated with the enumeration constant.
     */
    private final String sql;

    /**
     * Constructs a {@code PersonQuery} enumeration constant with the specified SQL query.
     *
     * @param sql the SQL query string.
     */
    PersonQuery(String sql) {
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
