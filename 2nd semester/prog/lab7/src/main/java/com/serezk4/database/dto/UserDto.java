package com.serezk4.database.dto;

import com.serezk4.database.model.User;

import java.io.Serializable;

/**
 * UserDto class. Represents a {@link User} data transfer object.
 * @param username
 * @param password
 */
public record UserDto(String username, String password) implements Serializable {
}
