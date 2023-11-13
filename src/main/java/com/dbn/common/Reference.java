package com.dbn.common;

public interface Reference<T extends Referenceable> {
    T get();
}
