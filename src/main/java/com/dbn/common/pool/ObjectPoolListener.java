package com.dbn.common.pool;

public interface ObjectPoolListener<T> {
    default void whenNull() {}

    default void whenCreated(T object) {}

    default void whenAcquired(T object){}

    default void whenReleased(T object) {}
}
