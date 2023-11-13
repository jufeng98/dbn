package com.dbn.data.model;

import com.dbn.common.dispose.UnlistedDisposable;
import com.dbn.data.type.DBDataType;

public interface ColumnInfo extends UnlistedDisposable {
    String getName();
    int getIndex();
    DBDataType getDataType();

    boolean isSortable();
}
