package com.serezk4.database.dto;

import com.serezk4.database.model.User;

import java.io.Serializable;

/**
 * Represents a Data Transfer Object (DTO) for a {@link User}.
 *
 * <p>The {@code UserDto} class is a record that holds the username and password of a user.
 * It implements {@link Serializable} to allow the object to be serialized for transportation
 * or storage.</p>
 *
 * <p>Usage:</p>
 * <pre>
 * UserDto userDto = new UserDto("username", "password");
 * String username = userDto.username();
 * String password = userDto.password();
 * </pre>
 *
 * @param username the username of the user.
 * @param password the password of the user.
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public record UserDto(String username, String password) implements Serializable {
}
