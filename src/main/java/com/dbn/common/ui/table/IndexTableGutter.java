package com.dbn.common.ui.table;

import javax.swing.*;

public class IndexTableGutter<T extends DBNTableWithGutter<?>> extends DBNTableGutter<T>{
    public IndexTableGutter(T table) {
        super(table);
    }

    @Override
    protected ListCellRenderer<?> createCellRenderer() {
        return new IndexTableGutterCellRenderer();
    }
}
