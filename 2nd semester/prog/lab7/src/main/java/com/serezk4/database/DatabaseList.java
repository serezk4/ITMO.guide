package com.serezk4.database;

import com.serezk4.database.model.Person;
import com.serezk4.database.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * DatabaseList class.
 * Extends {@link ArrayList} of {@link Person}.
 * Overrides {@link #add(Person)}, {@link #remove(Object)}, {@link #remove(int)} methods.
 */
public class DatabaseList extends ArrayList<Person> {
    /**
     * Basic logger for {@link DatabaseList} class.
     */
    private static final Logger log = LoggerFactory.getLogger(DatabaseList.class);

    /**
     * Ensures that this collection contains the specified element (optional operation).
     * @param person element whose presence in this collection is to be ensured
     * @return true if this collection changed as a result of the call
     */
    @Override
    public boolean add(Person person) {
        if (person == null) return false;
        boolean result = PersonService.getInstance().save(person).map(super::add).orElse(false);
        log.info("Person added: {}", person);
        return result;
    }

    /**
     * Removes the element at the specified position in this list (optional operation).
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     */
    @Override
    public Person remove(int index) {
        Person person = get(index);
        PersonService.getInstance().removeById(person.getId());
        log.info("Person removed: {}", person);
        return super.remove(index);
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     * @param o element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;
        PersonService.getInstance().removeById(person.getId());
        log.info("Person removed: {}", person);
        return super.remove(o);
    }

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     */
    @Deprecated
    @Override
    public void clear() {
        log.error("NI (not implemented) method clear");
        throw new UnsupportedOperationException("NI (not implemented) method clear");
    }


}