package com.dbn.common.latent;

@FunctionalInterface
public interface Loader<T> {
    T load();
}
