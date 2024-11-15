package com.serezk4.database.mapper;

import com.serezk4.database.dto.UserDto;
import com.serezk4.database.model.User;

/**
 * UserMapper class. Maps {@link User} to {@link UserDto} and vice versa.
 * implements {@link Mapper} interface.
 */
public class UserMapper implements Mapper<User, UserDto> {
    @Override
    public UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword());
    }

    @Override
    public User fromDto(UserDto userDto) {
        return new User(userDto.username(), userDto.password());
    }
}
