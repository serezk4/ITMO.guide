package com.serezk4.database.model;

import java.io.Serializable;

/**
 * Represents a user entity in the system.
 *
 * <p>The {@code User} class holds information about a user, including their unique ID,
 * username, and password. It implements {@link Serializable}, allowing it to be serialized
 * for transportation or storage.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * User user = new User(1L, "username", "password");
 * user.setPassword("newPassword");
 * System.out.println(user.getUsername());
 * </pre>
 *
 * <p>Thread Safety:</p>
 * <p>This class is not thread-safe. External synchronization is required if accessed
 * concurrently.</p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class User implements Serializable {

    /**
     * The unique identifier for the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * Constructs a {@code User} with all attributes.
     *
     * @param id       the unique identifier of the user.
     * @param username the username of the user.
     * @param password the password of the user.
     */
    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a {@code User} with a username and password.
     *
     * <p>This constructor can be used when the ID is not immediately required,
     * such as during user creation before persistence.</p>
     *
     * @param username the username of the user.
     * @param password the password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the user's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the username of the user.
     *
     * @param username the new username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password of the user.
     *
     * @param password the new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
