/**
 * Root package for database management utilities.
 *
 * <p>This package contains core classes and sub-packages for handling database
 * connections, repositories, models, queries, services, and data transfer objects (DTOs).
 * It provides a complete solution for managing entities and database operations.</p>
 *
 * <p>Main components:</p>
 * <ul>
 *     <li>{@link com.serezk4.database.ConnectionManager}: Manages database connections.</li>
 *     <li>{@link com.serezk4.database.DatabaseList}: Provides a list abstraction with database synchronization.</li>
 * </ul>
 *
 * <p>Sub-packages:</p>
 * <ul>
 *     <li>{@code dto}: Contains data transfer objects for communication between layers.</li>
 *     <li>{@code mapper}: Defines mapping logic between entities and DTOs.</li>
 *     <li>{@code model}: Defines database entities and related domain objects.</li>
 *     <li>{@code query}: Centralizes SQL query definitions.</li>
 *     <li>{@code repository}: Provides repository interfaces for data access.</li>
 *     <li>{@code service}: Implements services for managing business logic.</li>
 * </ul>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
package com.serezk4.database;
