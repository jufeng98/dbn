package com.dbn.common.filter;

import com.dbn.common.sign.Signed;
import lombok.SneakyThrows;

import java.util.Collection;

public interface Filter<T> extends Signed {
    boolean accepts(T object);

    @Override
    @SneakyThrows
    default int getSignature() {
        return hashCode();
    }

    default boolean acceptsAll(Collection<T> objects) {
        for (T object : objects) {
            if (!accepts(object)) return false;
        }
        return true;
    }

    default boolean isEmpty() {
        return false;
    }
}
