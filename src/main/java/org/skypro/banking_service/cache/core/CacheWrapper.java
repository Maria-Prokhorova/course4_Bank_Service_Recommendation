package org.skypro.banking_service.cache.core;

import java.util.function.Function;

public interface CacheWrapper<K, V> {
    V get(K key, Function<K, V> loader);

    void invalidateAll();

    V getIfPresent(K key);
}
