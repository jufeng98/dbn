package com.dbn.execution.method;

public interface ArgumentValueHolder<T> {
    T getValue();
    void setValue(T value);
}
