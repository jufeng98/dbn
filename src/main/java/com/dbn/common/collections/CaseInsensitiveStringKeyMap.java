package com.dbn.common.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveStringKeyMap<V> implements Map<String, V> {
    private final Map<String, V> data = new HashMap<>();

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(internalKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return data.get(internalKey(key));
    }

    @Nullable
    @Override
    public V put(String key, V value) {
        return data.put(internalKey(key), value);
    }

    @Override
    public V remove(Object key) {
        return data.remove(internalKey(key));
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends V> m) {
        m.forEach((k, v) -> data.put(internalKey(k), v));
    }

    @Override
    public void clear() {
        data.clear();
        entrySet().clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return data.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, V>> entrySet() {
        return data.entrySet();
    }

    private static String internalKey(Object key) {
        return key == null ? "" : key.toString().toLowerCase();
    }
}
