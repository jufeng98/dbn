package com.dbn.common.ui.table;

import com.dbn.common.ui.component.DBNComponent;
import org.jetbrains.annotations.NotNull;

public class DBNTableWithGutter<T extends DBNTableWithGutterModel> extends DBNTable<T>{
    public DBNTableWithGutter(DBNComponent parent, T tableModel, boolean showHeader) {
        super(parent, tableModel, showHeader);
    }

    @NotNull
    @Override
    public T getModel() {
        return super.getModel();
    }
}
