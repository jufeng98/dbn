package com.dbn.common.cache;

public class CacheValue<T> {
    private final T value;
    private final long timestamp;

    CacheValue(T value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public T getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isOlderThan(long expiryTimeMillis) {
        return expiryTimeMillis > 0 && timestamp + expiryTimeMillis < System.currentTimeMillis();
    }
}
