package com.serezk4.collection.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Location class.
 */
public class Location implements Comparable<Location>, Serializable {
    /**
     * X coordinate.
     *
     * @restriction Filed can't be null.
     */
    private Float x;
    /**
     * Y coordinate.
     *
     * @restriction Field can't be null.
     */
    private Double y;
    /**
     * Name.
     *
     * @restriction Field may be null.
     */
    private String name;

    /**
     * get X coordinate.
     *
     * @return X coordinate.
     */
    public Float getX() {
        return x;
    }

    /**
     * set X coordinate.
     *
     * @param x X coordinate.
     */
    public void setX(Float x) {
        if (x == null) throw new IllegalArgumentException("X can't be null");
        this.x = x;
    }

    /**
     * get Y coordinate.
     *
     * @return Y coordinate.
     */
    public Double getY() {
        return y;
    }

    /**
     * set Y coordinate.
     *
     * @param y Y coordinate.
     */
    public void setY(Double y) {
        if (y == null) throw new IllegalArgumentException("Y can't be null");
        this.y = y;
    }

    /**
     * get name.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * set name.
     *
     * @param name name.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(x, location.x) && Objects.equals(y, location.y) && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public int compareTo(Location o) {
        return Double.compare(this.x + this.y, o.x + o.y);
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}
