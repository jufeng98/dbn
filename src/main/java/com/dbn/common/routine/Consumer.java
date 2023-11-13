package com.dbn.common.routine;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@FunctionalInterface
public interface Consumer<T> extends java.util.function.Consumer<T> {

    default void acceptAll(@Nullable Collection<? extends T> collection) {
        if (collection != null) {
            for (T e : collection) {
                accept(e);
            }
        }
    }
}
