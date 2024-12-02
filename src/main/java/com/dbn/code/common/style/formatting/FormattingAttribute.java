package com.dbn.code.common.style.formatting;

public interface FormattingAttribute<T> {
    T getValue();

    abstract class Loader<T> {
        abstract T load();
    }
}
