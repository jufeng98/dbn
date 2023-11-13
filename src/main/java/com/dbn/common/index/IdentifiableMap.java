package com.dbn.common.index;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IdentifiableMap<K, V extends Identifiable<K>>{
    private Map<K, V> data = new HashMap<>();

    public void rebuild(Collection<V> values) {
        rebuild(values.stream());
    }

    public void rebuild(Stream<V> values) {
        this.data = values.collect(Collectors.toMap(v -> v.getId(), v -> v));
    }

    public void clear() {
        data.clear();
    }

    public V get(K id) {
        return id == null ? null : data.get(id);
    }

    public boolean contains(K key) {
        return data.containsKey(key);
    }
}
