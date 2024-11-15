package com.serezk4.configuration;

/**
 * Configuration class for recursion depth settings.
 * <p>
 * This class defines a constant for the maximum recursion depth allowed in the application.
 * It is used to prevent stack overflow errors caused by excessive recursion.
 * </p>
 *
 * @since 1.0
 */
public final class RecursionConfiguration {

    /**
     * Maximum depth allowed for recursive operations.
     * <p>
     * This value defines the limit on the number of recursive calls that can
     * be made within a single operation to ensure system stability.
     * </p>
     */
    public static final int MAX_RECURSION_DEPTH = 20;

    /**
     * Unit-class
     */
    private RecursionConfiguration() {
    }
}
