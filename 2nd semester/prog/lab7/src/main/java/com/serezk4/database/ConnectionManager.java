package com.serezk4.database;

import com.serezk4.configuration.DatabaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Manages the database connection and provides utility methods for preparing SQL statements.
 *
 * <p>The {@code ConnectionManager} class implements the Singleton pattern to ensure that only
 * one instance of the connection to the database exists. It initializes the database connection
 * using credentials and configurations provided by the {@link DatabaseConfiguration} class.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Manages a single instance of the database connection.</li>
 *     <li>Prepares SQL statements with or without parameters for execution.</li>
 *     <li>Ensures that the database connection is always available by re-establishing it if it
 *         is closed.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * ConnectionManager manager = ConnectionManager.getInstance();
 * PreparedStatement stmt = manager.prepare("SELECT * FROM users WHERE id = ?");
 * stmt.setInt(1, userId);
 * ResultSet rs = stmt.executeQuery();
 * </pre>
 *
 * @author serezk4
 * @see DatabaseConfiguration
 */
public final class ConnectionManager {
    /**
     * Logger for recording database connection activities and errors.
     */
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class.getName());
    /**
     * The singleton instance of the {@link ConnectionManager}.
     */
    private static ConnectionManager instance;
    /**
     * The active database connection managed by this class.
     */
    private Connection connection;

    /**
     * Initializes the database connection.
     *
     * <p>The constructor is private to enforce the Singleton pattern. It loads the PostgreSQL
     * JDBC driver and establishes a connection to the database using the credentials and URL
     * from {@link DatabaseConfiguration}.</p>
     *
     * <p>If the driver cannot be loaded or the connection cannot be established, an error is logged,
     * and the application exits with a status code of 1.</p>
     */
    private ConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    DatabaseConfiguration.POSTGRES_URL,
                    DatabaseConfiguration.POSTGRES_USER,
                    DatabaseConfiguration.POSTGRES_PASSWORD
            );
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database connection error: {}", e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Returns the singleton instance of the {@code ConnectionManager}.
     *
     * <p>This method ensures that only one instance of the {@code ConnectionManager} exists. If the
     * instance is {@code null} or the current connection is closed, a new instance is created.</p>
     *
     * <p>Thread Safety:</p>
     * <p>This method is not thread-safe. If multiple threads access this method simultaneously, it
     * may result in multiple instances being created. Use synchronization or other mechanisms if
     * thread safety is required.</p>
     *
     * @return the singleton instance of the {@link ConnectionManager}.
     * @throws SQLException if an error occurs while checking the connection state or creating a new instance.
     */
    public static ConnectionManager getInstance() throws SQLException {
        return (instance == null || instance.connection.isClosed()) ? instance = new ConnectionManager() : instance;
    }

    /**
     * Prepares a SQL statement for execution.
     *
     * <p>This method creates a {@link PreparedStatement} for the provided SQL query using the
     * managed database connection.</p>
     *
     * @param query the SQL query to prepare.
     * @return a {@link PreparedStatement} object for the given query.
     * @throws SQLException if an error occurs while preparing the statement.
     */
    public PreparedStatement prepare(final String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    /**
     * Prepares a SQL statement with parameters for execution.
     *
     * <p>This method creates a {@link PreparedStatement} for the provided SQL query and sets the
     * specified parameters in the statement.</p>
     *
     * <p>Workflow:</p>
     * <ul>
     *     <li>Creates a {@link PreparedStatement} using the {@link #prepare(String)} method.</li>
     *     <li>Iterates through the provided parameters and sets them in the statement using
     *         {@code setObject}.</li>
     * </ul>
     *
     * <p>Example:</p>
     * <pre>
     * PreparedStatement stmt = manager.prepare(
     *     "INSERT INTO users (name, age) VALUES (?, ?)", "John Doe", 30
     * );
     * stmt.executeUpdate();
     * </pre>
     *
     * @param query      the SQL query to prepare.
     * @param parameters the parameters to set in the prepared statement.
     * @return a {@link PreparedStatement} object with the given query and parameters set.
     * @throws SQLException if an error occurs while preparing the statement or setting parameters.
     */
    public PreparedStatement prepare(String query, Object... parameters) throws SQLException {
        PreparedStatement stmt = ConnectionManager.getInstance().prepare(query);
        for (int i = 0; i < parameters.length; i++) stmt.setObject(i + 1, parameters[i]);
        return stmt;
    }
}
