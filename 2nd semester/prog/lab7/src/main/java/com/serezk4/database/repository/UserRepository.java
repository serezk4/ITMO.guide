package com.serezk4.database.repository;

import com.serezk4.database.model.User;

import java.util.List;
import java.util.Optional;

/**
 * {@link User} repository.
 */
public interface UserRepository {
    List<User> findAll();

    void save(String username, String password);
    boolean existsByUsername(String username);

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    boolean checkPassword(User user, String password);
}
