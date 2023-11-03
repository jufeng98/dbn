package com.dci.intellij.dbn.common.ref;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface WeakRefCache<K, V> {
    V get(K key);

    V get(K key, Function<K, V> loader);

    V compute(K key, BiFunction<K, V, V> loader);

    V computeIfAbsent(K key, Function<? super K, ? extends V> loader);

    void set(K key, V value);

    V remove(K key);

    boolean contains(K key);

    Set<K> keys();

    void clear();

    static <K, V> WeakRefCache<K, V> weakKey() {
        return new WeakRefCacheKeyImpl<>();
    }

    static <K, V> WeakRefCache<K, V> weakKeyValue() {
        return new WeakRefCacheKeyValueImpl<>();
    }
}
