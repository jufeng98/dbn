package com.dbn.object;

import com.dbn.object.common.DBObject;

import java.util.List;

public interface DBNestedTable extends DBObject {
    List<DBNestedTableColumn> getColumns();
    DBNestedTableColumn getColumn(String name);

    DBTable getTable();
}