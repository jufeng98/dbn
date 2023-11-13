package com.dbn.common.ui;

import java.util.EventListener;

public interface ValueSelectorListener<T> extends EventListener {
    void selectionChanged(T oldValue, T newValue);
}
