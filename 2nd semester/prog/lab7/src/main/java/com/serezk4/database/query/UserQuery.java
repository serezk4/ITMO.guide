package com.serezk4.database.query;

import com.serezk4.database.model.User;

/**
 * {@link User} query enumeration.
 */
public enum UserQuery {
    /**
     * Save {@link User} query.
     */
    SAVE_USER("INSERT INTO users (username, password) VALUES (?, ?)"),

    /**
     * Check if {@link User} exists by {@link User#getUsername()} query.
     */
    EXISTS_BY_USERNAME("SELECT COUNT(*) FROM users WHERE username = ?"),

    /**
     * Find {@link User} by {@link User#getId()} query.
     */
    FIND_BY_ID("SELECT * FROM users WHERE id = ?"),

    /**
     * Find {@link User} by {@link User#getUsername()} query.
     */
    FIND_BY_USERNAME("SELECT * FROM users WHERE username = ?"),

    /**
     * Find all {@link User} query.
     */
    FIND_ALL("SELECT * FROM users");

    /**
     * SQL query.
     */
    private final String sql;

    UserQuery(String sql) {
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
