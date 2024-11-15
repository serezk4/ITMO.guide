package com.serezk4.database.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Color enumeration.
 */
public enum Color {
    GREEN,
    BLUE,
    YELLOW,
    ORANGE,
    WHITE;

    public static String formattedList() {
        return Arrays.stream(Color.values())
                .map(val -> ", ".concat(String.valueOf(val)))
                .collect(Collectors.joining()).substring(2);
    }
}