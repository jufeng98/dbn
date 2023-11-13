package com.dbn.common.latent.impl;

import com.dbn.common.latent.Latent;
import com.dbn.common.latent.Loader;
import com.dbn.common.ref.WeakRef;

public class WeakRefLatent<T> implements Latent<T> {
    private final Loader<T> loader;
    private WeakRef<T> value;
    private volatile boolean loaded;

    public WeakRefLatent(Loader<T> loader) {
        this.loader = loader;
    }

    public final T get() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    T value = loader == null ? null : loader.load();
                    this.value = WeakRef.of(value);
                    loaded = true;
                }
            }
        }
        return WeakRef.get(value);
    }

    public final void set(T value) {
        this.value = WeakRef.of(value);
        loaded = true;
    }

    public final boolean loaded() {
        return loaded;
    }

    public void reset() {
        value = null;
        loaded = false;
    }

    @Override
    public T value() {
        return WeakRef.get(value);
    }
}
