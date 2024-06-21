package com.serezk4.collection.model;

import com.serezk4.collection.id.IdGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Person class.
 *
 * @see Coordinates
 * @see Color
 * @see Country
 * @see Location
 */
public class Person implements Comparable<Person> {
    /**
     * Person's id.
     *
     * @restriction Field must be greater than 0.
     * @restriction Field must be unique.
     * @restriction Field must be generated automatically.
     * @restriction Field can't be null.
     */
    private Long id;

    {
        try {
            id = IdGenerator.getInstance().generateId();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            id = -1L;
        }
    }

    /**
     * get Person's id.
     *
     * @return Person's id.
     */
    public Long getId() {
        return id;
    }

    /**
     * set Person's id.
     *
     * @param id Person's id.
     * @throws IllegalArgumentException if id is null or less than or equal to 0
     */
    public void setId(Long id) {
        if (id == null) throw new IllegalArgumentException("Id can't be null");
        if (id <= 0) throw new IllegalArgumentException("Id must be greater than 0");
        this.id = id;
    }

    /**
     * Person's name.
     *
     * @restriction Field can't be null.
     * @restriction Field can't be empty.
     */
    private String name;

    /**
     * get Person's name.
     *
     * @return Person's name.
     */
    public String getName() {
        return name;
    }

    /**
     * set Person's name.
     *
     * @param name Person's name.
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("Name can't be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name can't be empty");
        this.name = name;
    }

    /**
     * Person's coordinates.
     *
     * @restriction Field can't be null.
     */
    private Coordinates coordinates;

    /**
     * get Person's coordinates.
     *
     * @return Person's coordinates.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * set Person's coordinates.
     *
     * @param coordinates Person's coordinates.
     * @throws IllegalArgumentException if coordinates is null
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new IllegalArgumentException("Coordinates can't be null");
        this.coordinates = coordinates;
    }

    /**
     * Person's creation date.
     *
     * @restriction Field can't be null.
     * @restriction Field must be generated automatically.
     */
    private final LocalDate creationDate = LocalDate.now(ZoneId.systemDefault());

    /**
     * get Person's creation date.
     *
     * @return Person's creation date.
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Person's height.
     *
     * @restriction Field can't be null.
     * @restriction Field must be greater than 0.
     */
    private Integer height;

    /**
     * get Person's height.
     *
     * @return Person's height.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * set Person's height.
     *
     * @param height Person's height.
     * @throws IllegalArgumentException if height is null or less than or equal to 0
     */
    public void setHeight(Integer height) {
        if (height == null) throw new IllegalArgumentException("Height can't be null");
        if (height <= 0) throw new IllegalArgumentException("Height must be greater than 0");
        this.height = height;
    }

    /**
     * Person's weight.
     *
     * @restriction Field must be greater than 0.
     */
    private int weight;

    /**
     * get Person's weight.
     *
     * @return Person's weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * set Person's weight.
     *
     * @param weight Person's weight.
     * @throws IllegalArgumentException if weight is less than or equal to 0
     */
    public void setWeight(int weight) {
        if (weight <= 0) throw new IllegalArgumentException("Weight must be greater than 0");
        this.weight = weight;
    }

    /**
     * Person's eye color.
     *
     * @restriction Field can't be null.
     */
    private Color hairColor;

    /**
     * get Person's eye color.
     *
     * @return Person's eye color.
     */
    public Color getHairColor() {
        return hairColor;
    }

    /**
     * set Person's eye color.
     *
     * @param hairColor Person's eye color.
     * @throws IllegalArgumentException if hairColor is null
     */
    public void setHairColor(Color hairColor) {
        if (hairColor == null) throw new IllegalArgumentException("Hair color can't be null");
        this.hairColor = hairColor;
    }

    /**
     * Person's country
     */
    private Country nationality;

    /**
     * get Person's country
     *
     * @return Person's country
     */
    public Country getNationality() {
        return nationality;
    }

    /**
     * set Person's country
     *
     * @param nationality Person's country
     */
    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    /**
     * Person's location.
     */
    private Location location;

    /**
     * get Person's location.
     * @return Person's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * set Person's location.
     * @param location Person's location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return weight == person.weight && Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(coordinates, person.coordinates) && Objects.equals(creationDate, person.creationDate) && Objects.equals(height, person.height) && hairColor == person.hairColor && nationality == person.nationality && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, height, weight, hairColor, nationality, location);
    }


    /**
     * Compares this object with the specified object for body mass index.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Person o) {
        return Double.compare(this.weight / Math.pow(this.height, 2), o.weight / Math.pow(o.height, 2));
    }
}