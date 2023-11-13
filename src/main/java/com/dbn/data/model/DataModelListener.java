package com.dbn.data.model;

import java.util.EventListener;

public interface DataModelListener extends EventListener {
    void modelChanged();
}
