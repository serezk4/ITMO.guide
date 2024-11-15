package com.serezk4.database.model;

import java.io.Serializable;

/**
 * User class.
 */
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;

    /**
     * Default constructor with all arguments.
     * @param id {@link String} user id
     * @param username {@link String} user name
     * @param password {@link String} user password
     */
    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor with username and password.
     * @param username {@link String} user name
     * @param password {@link String} user password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
