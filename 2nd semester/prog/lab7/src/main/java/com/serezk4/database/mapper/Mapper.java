package com.serezk4.database.mapper;

/**
 * Mapper interface.
 * @param <K> - the type of the source object
 * @param <V> - the type of the dto object
 */
public interface Mapper<K, V> {
    V toDto(K k);
    K fromDto(V v);
}
