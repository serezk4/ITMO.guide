package com.serezk4.database.query;

import com.serezk4.database.model.Person;

/**
 * {@link Person} query enumeration.
 */
public enum PersonQuery {
    /**
     * Find all {@link Person} query.
     */
    FIND_ALL("SELECT * FROM persons"),

    /**
     * Save {@link Person} query.
     */
    SAVE_PERSON("INSERT INTO persons (owner_id, name, cord_x, cord_y, height, weight, color, country, location_x, location_y, location_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id"),

    /**
     * Remove {@link Person} by id query.
     */
    REMOVE_BY_ID("DELETE FROM persons WHERE id = ?");


    /**
     * SQL query.
     */
    private final String sql;

    PersonQuery(String sql) {
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
