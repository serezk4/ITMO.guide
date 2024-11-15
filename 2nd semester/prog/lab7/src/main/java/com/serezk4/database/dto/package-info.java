/**
 * Package for data transfer objects (DTOs).
 *
 * <p>The {@code dto} package contains simple, serializable objects designed for
 * transferring data between layers of the application. These objects decouple
 * the service and repository layers from the domain model, enabling flexibility
 * and scalability.</p>
 *
 * <p>Main components:</p>
 * <ul>
 *     <li>{@link com.serezk4.database.dto.UserDto}: Represents a user data transfer object.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * UserDto userDto = new UserDto("username", "password");
 * </pre>
 *
 * @see com.serezk4.database.mapper
 */
package com.serezk4.database.dto;
