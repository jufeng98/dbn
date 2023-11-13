package com.dbn.common.thread;

public class ThreadLocalFlag {
    private final boolean defaultValue;
    private final ThreadLocal<Boolean> flag = new ThreadLocal<>();

    public ThreadLocalFlag(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean get() {
        Boolean value = flag.get();
        return value == null ? defaultValue : value;
    }

    public void set(boolean value) {
        flag.set(value);
    }

}
