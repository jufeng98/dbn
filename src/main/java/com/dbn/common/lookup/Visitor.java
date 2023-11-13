package com.dbn.common.lookup;

public interface Visitor<T> {
    void visit(T element);
}
