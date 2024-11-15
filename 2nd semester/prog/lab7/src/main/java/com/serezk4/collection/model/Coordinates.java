package com.serezk4.collection.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Coordinates class.
 */
public class Coordinates implements Serializable {

    /**
     * X coordinate.
     * <p>
     *
     * @restriction Value must be > -271
     * @restriction Field can't be null
     */
    private Integer x;

    /**
     * Y coordinate.
     * <p>
     *
     * @restriction Field can't be null
     */
    private Integer y;

    /**
     * get X coordinate.
     *
     * @return X coordinate.
     */
    public Integer getX() {
        return x;
    }

    /**
     * set X coordinate.
     *
     * @param x X coordinate.
     */
    public void setX(Integer x) {
        if (x == null) throw new IllegalArgumentException("X can't be null");
        if (x <= -271) throw new IllegalArgumentException("X must be > -271");
        this.x = x;
    }

    /**
     * get Y coordinate.
     *
     * @return Y coordinate.
     */
    public Integer getY() {
        return y;
    }

    /**
     * set Y coordinate.
     *
     * @param y Y coordinate.
     */
    public void setY(Integer y) {
        if (y == null) throw new IllegalArgumentException("Y can't be null");
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of the {@code Coordinates} object.
     * <p>
     * The string includes the {@code x} and {@code y} coordinates, formatted in a compact style.
     * </p>
     *
     * @return a string representation of the {@code Coordinates} object
     */
    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                "}";
    }
}
