package com.dbn.data.model;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.data.type.DBDataType;

import java.util.List;

public interface DataModelHeader<T extends ColumnInfo> extends StatefulDisposable {
    List<T> getColumnInfos();

    T getColumnInfo(int index);

    T getColumnInfo(String name);

    int getColumnIndex(String name);

    String getColumnName(int index);

    DBDataType getColumnDataType(int index);

    int getColumnCount();
}
