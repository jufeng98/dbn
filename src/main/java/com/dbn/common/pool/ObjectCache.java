package com.dbn.common.pool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjectCache<K, V, E extends Throwable> {
    @Nullable
    V get(K key);

    @NotNull
    V ensure(K key) throws E;

    void drop(K key);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}
