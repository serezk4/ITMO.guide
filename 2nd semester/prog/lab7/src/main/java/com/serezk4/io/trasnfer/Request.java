package com.serezk4.io.trasnfer;

import com.serezk4.database.dto.UserDto;
import com.serezk4.database.model.Person;
import com.serezk4.database.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a request containing a command, its arguments, and associated persons.
 * <p>
 * This record encapsulates all necessary data to execute a command, including the command
 * name, optional arguments, and a list of {@link Person} objects.
 * </p>
 *
 * @see Person
 * @since 1.0
 */
public final class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private final String command;
    private final List<String> args;
    private final List<Person> persons;
    private final UserDto userDto;

    private transient User user;

    /**
     * @param command the name of the command to execute
     * @param args    the arguments required for the command
     * @param persons the list of {@link Person} objects associated with the command
     */
    public Request(String command, List<String> args, List<Person> persons, UserDto userDto) {
        this.command = command;
        this.args = args;
        this.persons = persons;
        this.userDto = userDto;
    }

    public String command() {
        return command;
    }

    public List<String> args() {
        return args;
    }

    public List<Person> persons() {
        return persons;
    }

    public UserDto userDto() {
        return userDto;
    }

    public User user() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Request) obj;
        return Objects.equals(this.command, that.command) &&
                Objects.equals(this.args, that.args) &&
                Objects.equals(this.persons, that.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, args, persons);
    }

    @Override
    public String toString() {
        return "Request[" +
                "command=" + command + ", " +
                "args=" + args + ", " +
                "persons=" + persons + ']';
    }

}
