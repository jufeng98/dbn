package com.dci.intellij.dbn.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.dci.intellij.dbn.common.util.Unsafe.cast;

/**
 * A cache implementation that supports chaining for multi-level cache lookups.
 * (usage only justified if value initialisation is really expensive)
 */
public class ChainedKeyCache {
    private final Map<Object, Object> data = new ConcurrentHashMap<>();

    public <T> T get(Function<Object[], T> loader, Object ... keys) {
        Map map = data;
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            if (i < keys.length - 1) {
                map = cast(map.computeIfAbsent(key, k -> new ConcurrentHashMap<>()));
            } else {
                return cast(map.computeIfAbsent(key, k -> loader.apply(keys)));
            }
        }
        return null;
    }
}
