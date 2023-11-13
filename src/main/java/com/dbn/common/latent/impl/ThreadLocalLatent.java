package com.dbn.common.latent.impl;

import com.dbn.common.latent.Latent;
import com.dbn.common.latent.Loader;

public final class ThreadLocalLatent<T> implements Latent<T> {
    private final Loader<T> loader;
    private final ThreadLocal<T> value = new ThreadLocal<>();

    public ThreadLocalLatent(Loader<T> loader) {
        this.loader = loader;
    }

    @Override
    public void set(T value) {
        this.value.set(value);
    }

    @Override
    public void reset() {
        this.value.remove();
    }

    @Override
    public boolean loaded() {
        return this.value.get() != null;
    }

    public T get(){
        T value = this.value.get();
        if (value == null) {
            value = loader.load();
            this.value.set(value);
        }
        return value;
    }

    @Override
    public T value() {
        return value.get();
    }
}
