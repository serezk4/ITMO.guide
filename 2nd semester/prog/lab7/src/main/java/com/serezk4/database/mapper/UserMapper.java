package com.serezk4.database.mapper;

import com.serezk4.database.dto.UserDto;
import com.serezk4.database.model.User;

/**
 * Maps {@link User} entities to {@link UserDto} objects and vice versa.
 *
 * <p>The {@code UserMapper} class provides methods to convert a {@link User} entity into a
 * {@link UserDto} for data transfer purposes, and to reconstruct a {@link User} entity from
 * a {@link UserDto}. It implements the {@link Mapper} interface.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * UserMapper userMapper = new UserMapper();
 *
 * User user = new User("username", "password");
 * UserDto userDto = userMapper.toDto(user);
 *
 * User reconstructedUser = userMapper.fromDto(userDto);
 * </pre>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class UserMapper implements Mapper<User, UserDto> {

    /**
     * Converts a {@link User} entity into a {@link UserDto}.
     *
     * @param user the {@link User} entity to convert.
     * @return a {@link UserDto} containing the {@link User}'s data.
     */
    @Override
    public UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword());
    }

    /**
     * Converts a {@link UserDto} into a {@link User} entity.
     *
     * @param userDto the {@link UserDto} to convert.
     * @return a {@link User} entity reconstructed from the {@link UserDto}.
     */
    @Override
    public User fromDto(UserDto userDto) {
        return new User(userDto.username(), userDto.password());
    }
}
