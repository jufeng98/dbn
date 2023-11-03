package com.dci.intellij.dbn.common.ref;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

abstract class WeakRefCacheBase<K, V> implements WeakRefCache<K, V> {
    private final Map<K, V> cache = createCache();

    protected abstract Map<K, V> createCache();

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    @SneakyThrows
    public V get(K key, Function<K, V> loader) {
        return cache.computeIfAbsent(key, loader);
    }

    @Override
    public V compute(K key, BiFunction<K, V, V> loader) {
        return cache.compute(key, loader);
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> loader) {
        return cache.computeIfAbsent(key, loader);
    }

    @Override
    public void set(K key, @Nullable V value) {
        if (value == null)
            cache.remove(key);
        else
            cache.put(key, value);
    }

    @Override
    public V remove(K key) {
        return cache.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    @Override
    public Set<K> keys() {
        return cache.keySet();
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
