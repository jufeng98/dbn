package com.dbn.common.util;

public abstract class RefreshableValue<T>{
    private T value;
    private boolean loaded = false;
    private final int refreshInterval;
    private long lastRefreshTimestamp;

    public RefreshableValue(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public T get() {
        if (!loaded || lastRefreshTimestamp < System.currentTimeMillis() - refreshInterval) {
            value = Commons.nvln(load(), value);
            loaded = true;
            lastRefreshTimestamp = System.currentTimeMillis();
        }
        return value;
    }

    protected abstract T load();
}
