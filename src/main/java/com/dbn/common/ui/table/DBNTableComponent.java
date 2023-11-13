package com.dbn.common.ui.table;

import com.intellij.ui.components.JBScrollPane;

public class DBNTableComponent<T extends DBNTable> extends JBScrollPane{
    private T table;
    public DBNTableComponent(T table) {
        this.table = table;
    }

    public T getTable() {
        return table;
    }
}
