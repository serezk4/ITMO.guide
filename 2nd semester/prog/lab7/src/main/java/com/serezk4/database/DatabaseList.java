package com.serezk4.database;

import com.serezk4.database.model.Person;
import com.serezk4.database.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A specialized list for managing {@link Person} objects with database integration.
 *
 * <p>The {@code DatabaseList} class extends {@link ArrayList} and overrides key methods to
 * integrate with the {@link PersonService}. This ensures that changes to the list are reflected
 * in the database and vice versa.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Adds a {@link Person} to the list and persists it to the database.</li>
 *     <li>Removes a {@link Person} from the list and deletes it from the database by its ID.</li>
 *     <li>Overrides the {@link #clear()} method but marks it as deprecated since it is not implemented.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * DatabaseList personList = new DatabaseList();
 * Person person = new Person(1, "John Doe");
 * personList.add(person); // Adds person to the list and saves it to the database
 * personList.remove(person); // Removes person from the list and the database
 * </pre>
 *
 * <p>Thread Safety:</p>
 * <p>This class is not thread-safe. External synchronization is required if used in a
 * concurrent environment.</p>
 *
 * @see Person
 * @see PersonService
 * @author serezk4
 */
public class DatabaseList extends ArrayList<Person> {
    /**
     * Logger for recording actions performed on the {@code DatabaseList}.
     */
    private static final Logger log = LoggerFactory.getLogger(DatabaseList.class);

    /**
     * Adds a {@link Person} to the list and persists it to the database.
     *
     * <p>This method ensures that the specified {@link Person} is saved to the database using the
     * {@link PersonService}. If the save operation succeeds, the person is added to the list.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Checks if the {@code person} is not {@code null}.</li>
     *     <li>Calls {@link PersonService#save(Person)} to persist the person to the database.</li>
     *     <li>If successful, adds the person to the list and logs the action.</li>
     * </ul>
     *
     * @param person the {@link Person} to be added to the list.
     * @return {@code true} if the person was added to the list and saved to the database; {@code false} otherwise.
     */
    @Override
    public boolean add(Person person) {
        if (person == null) return false;
        boolean result = PersonService.getInstance().save(person).map(super::add).orElse(false);
        log.info("Person added: {}", person);
        return result;
    }

    /**
     * Removes the {@link Person} at the specified index and deletes it from the database.
     *
     * <p>This method retrieves the {@link Person} at the specified index, removes it from the list,
     * and deletes it from the database by its ID using the {@link PersonService}.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Retrieves the person at the specified index.</li>
     *     <li>Calls {@link PersonService#removeById(int)} to delete the person from the database.</li>
     *     <li>Removes the person from the list and logs the action.</li>
     * </ul>
     *
     * @param index the index of the {@link Person} to remove.
     * @return the {@link Person} that was removed from the list.
     */
    @Override
    public Person remove(int index) {
        Person person = get(index);
        PersonService.getInstance().removeById(person.getId());
        log.info("Person removed: {}", person);
        return super.remove(index);
    }

    /**
     * Removes the {@link Person} at the specified index and deletes it from the database.
     *
     * <p>This method retrieves the {@link Person} at the specified index, removes it from the list,
     * and deletes it from the database by its ID using the {@link PersonService}.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Retrieves the person at the specified index.</li>
     *     <li>Calls {@link PersonService#removeById(int)} to delete the person from the database.</li>
     *     <li>Removes the person from the list and logs the action.</li>
     * </ul>
     *
     * @param o object to remove
     * @return the {@link Person} that was removed from the list.
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
     * Clears all elements from the list.
     *
     * <p>This method is marked as deprecated and throws an {@link UnsupportedOperationException}
     * since it is not implemented. Clearing the list and database simultaneously is not supported
     * by this class.</p>
     *
     * @throws UnsupportedOperationException always thrown since this method is not implemented.
     * @deprecated Clearing all elements is not supported.
     */
    @Deprecated
    @Override
    public void clear() {
        log.error("NI (not implemented) method clear");
        throw new UnsupportedOperationException("NI (not implemented) method clear");
    }


}