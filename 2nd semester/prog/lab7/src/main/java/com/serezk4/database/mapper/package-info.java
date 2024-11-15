/**
 * Package for mapping entities to data transfer objects (DTOs) and vice versa.
 *
 * <p>The {@code mapper} package defines interfaces and classes for converting
 * database entities into DTOs and vice versa. This abstraction simplifies data
 * transformation logic and ensures consistency across layers.</p>
 *
 * <p>Main components:</p>
 * <ul>
 *     <li>{@link com.serezk4.database.mapper.Mapper}: Generic interface for mapping logic.</li>
 *     <li>{@link com.serezk4.database.mapper.UserMapper}: Maps {@link com.serezk4.database.model.User}
 *     to {@link com.serezk4.database.dto.UserDto}.</li>
 * </ul>
 *
 * @see com.serezk4.database.dto
 * @see com.serezk4.database.model
 */
package com.serezk4.database.mapper;
