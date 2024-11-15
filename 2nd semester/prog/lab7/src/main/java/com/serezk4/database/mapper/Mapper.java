package com.serezk4.database.mapper;

/**
 * Defines a generic contract for mapping between entities and their Data Transfer Objects (DTOs).
 *
 * <p>The {@code Mapper} interface provides methods to convert an entity of type {@code K} to its
 * corresponding DTO of type {@code V} and vice versa. It is designed to be implemented by classes
 * that handle such conversions for specific types.</p>
 *
 * <p>Type Parameters:</p>
 * <ul>
 *     <li>{@code K} - the type of the source entity.</li>
 *     <li>{@code V} - the type of the DTO object.</li>
 * </ul>
 *
 * <p>Usage Example:</p>
 * <pre>
 * public class UserMapper implements Mapper&lt;User, UserDto&gt; {
 *     {@literal @}Override
 *     public UserDto toDto(User user) {
 *         return new UserDto(user.getUsername(), user.getPassword());
 *     }
 *
 *     {@literal @}Override
 *     public User fromDto(UserDto userDto) {
 *         return new User(userDto.username(), userDto.password());
 *     }
 * }
 * </pre>
 *
 * @param <K> the type of the source object (e.g., entity).
 * @param <V> the type of the Data Transfer Object (DTO).
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public interface Mapper<K, V> {

    /**
     * Converts an entity of type {@code K} to a DTO of type {@code V}.
     *
     * @param k the source entity to be converted.
     * @return the corresponding DTO of type {@code V}.
     */
    V toDto(K k);

    /**
     * Converts a DTO of type {@code V} to an entity of type {@code K}.
     *
     * @param v the source DTO to be converted.
     * @return the corresponding entity of type {@code K}.
     */
    K fromDto(V v);
}
