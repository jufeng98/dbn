package com.dbn.common;

import java.util.Objects;
import java.util.function.Supplier;

public class Capture<T> {
    private T value;
    private Object signature;
    protected volatile boolean loading;

    public boolean isOutdated(Object signature) {
        return !Objects.equals(this.signature, signature);
    }

    public T get() {
        return value;
    }

    public void capture(Object check, Supplier<T> value) {
        this.signature = check;
        if (loading) return;
        synchronized (this) {
            if (loading) return;
            try {
                loading = true;
                this.value = value.get();
            } finally {
                loading = false;
            }
        }
    }


}
