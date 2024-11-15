package com.serezk4.database.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Color enumeration.
 */
public enum Country {
    USA,
    GERMANY,
    VATICAN,
    NORTH_KOREA;

    public static String formattedList() {
        return Arrays.stream(Country.values())
                .map(val -> ", ".concat(String.valueOf(val)))
                .collect(Collectors.joining()).substring(2);
    }
}