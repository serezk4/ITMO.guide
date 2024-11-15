package com.serezk4.configuration;

/**
 * Provides database configuration details for connecting to a PostgreSQL database.
 *
 * <p>The {@code DatabaseConfiguration} class retrieves connection details such as the URL,
 * username, and password from environment variables. These details are used to establish
 * a connection with the database.</p>
 *
 * <p>Environment Variables:</p>
 * <ul>
 *     <li>{@code DB_HOST}: The hostname or IP address of the database server.</li>
 *     <li>{@code DB_PORT}: The port number on which the database server is running.</li>
 *     <li>{@code DB_NAME}: The name of the database to connect to.</li>
 *     <li>{@code DB_USER}: The username for authentication.</li>
 *     <li>{@code DB_PASSWORD}: The password for authentication.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * String url = DatabaseConfiguration.POSTGRES_URL;
 * String user = DatabaseConfiguration.POSTGRES_USER;
 * String password = DatabaseConfiguration.POSTGRES_PASSWORD;
 * </pre>
 *
 * <p>All fields are {@code public static final}, allowing direct access without creating an
 * instance of the class.</p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class DatabaseConfiguration {

    /**
     * The JDBC URL for connecting to the PostgreSQL database.
     *
     * <p>The URL is constructed using the {@code DB_HOST}, {@code DB_PORT}, and {@code DB_NAME}
     * environment variables. It follows the format:</p>
     * <pre>
     * jdbc:postgresql://<DB_HOST>:<DB_PORT>/<DB_NAME>
     * </pre>
     *
     * <p>Example:</p>
     * <pre>
     * jdbc:postgresql://localhost:5432/mydatabase
     * </pre>
     *
     * @see <a href="https://jdbc.postgresql.org/documentation/head/connect.html">
     * PostgreSQL JDBC Connection</a>
     */
    public static final String POSTGRES_URL = "jdbc:postgresql://%s:%s/%s".formatted(
            System.getenv("DB_HOST"),
            System.getenv("DB_PORT"),
            System.getenv("DB_NAME")
    );
    /**
     * The username for connecting to the PostgreSQL database.
     *
     * <p>Retrieved from the {@code DB_USER} environment variable.</p>
     */
    public static final String POSTGRES_USER = System.getenv("DB_USER");
    /**
     * The password for connecting to the PostgreSQL database.
     *
     * <p>Retrieved from the {@code DB_PASSWORD} environment variable.</p>
     */
    public static final String POSTGRES_PASSWORD = System.getenv("DB_PASSWORD");

    private DatabaseConfiguration() {
    }
}
