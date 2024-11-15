package com.serezk4.database.repository;


import com.serezk4.database.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * {@link Person} repository.
 */
public interface PersonRepository {
    List<Person> findAll();
    Optional<Person> save(Person person);
    void removeById(int id);
}
