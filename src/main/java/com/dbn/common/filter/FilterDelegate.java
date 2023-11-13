package com.dbn.common.filter;

public interface FilterDelegate<T> extends Filter<T> {
    Filter<T> inner();

    default int getSignature() {
        Filter<T> inner = inner();
        return inner == null ? 0 : inner.getSignature();
    }

    @Override
    default boolean accepts(T object) {
        Filter<T> inner = inner();
        return inner == null || inner.accepts(object);
    }
}
