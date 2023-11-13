package com.dbn.connection.jdbc;

import com.dbn.common.util.TimeUtil;

public abstract class IntervalLoader<T> {
    private T value;
    private long checkTimestamp;
    private final long checkInterval;

    public IntervalLoader(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public final T get() {
        long currentTimeMillis = System.currentTimeMillis();
        if (TimeUtil.isOlderThan(checkTimestamp, checkInterval)) {
            checkTimestamp = currentTimeMillis;
            value = load();
        }
        return value;
    }

    public T getValue() {
        return value;
    }

    protected abstract T load();
}
