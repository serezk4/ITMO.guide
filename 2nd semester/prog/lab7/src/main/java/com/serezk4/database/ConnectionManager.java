package com.serezk4.database;

import com.serezk4.database.configuration.DatabaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ConnectionManager class.
 * Manages connection to the database.
 */
public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class.getName());
    private Connection connection;
    private static ConnectionManager instance;

    /**
     * Base constructor.
     */
    private ConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(DatabaseConfiguration.POSTGRES_URL, DatabaseConfiguration.POSTGRES_USER, DatabaseConfiguration.POSTGRES_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database connection error: {}", e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Returns the instance of the ConnectionManager.
     *
     * @return the instance of the {@link ConnectionManager}.
     * @throws SQLException if an error occurred while getting the instance.
     */
    public static ConnectionManager getInstance() throws SQLException {
        return (instance == null || instance.connection.isClosed()) ? instance = new ConnectionManager() : instance;
    }

    /**
     * Prepares a statement.
     *
     * @param query the query to prepare.
     * @return the prepared statement.
     * @throws SQLException if an error occurred while preparing the statement.
     */
    public PreparedStatement prepare(final String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    /**
     * Prepares a statement with parameters.
     *
     * @param query      the query to prepare.
     * @param parameters the parameters to set.
     * @return the prepared statement.
     * @throws SQLException if an error occurred while preparing the statement.
     */
    public PreparedStatement prepare(String query, Object... parameters) throws SQLException {
        PreparedStatement stmt = ConnectionManager.getInstance().prepare(query);
        for (int i = 0; i < parameters.length; i++) stmt.setObject(i + 1, parameters[i]);
        return stmt;
    }
}
