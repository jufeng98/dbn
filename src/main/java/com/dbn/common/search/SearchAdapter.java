package com.dbn.common.search;

public interface SearchAdapter<T> {
    int evaluate(T element);
}
