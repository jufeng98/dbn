package com.dbn.object;

import com.dbn.object.common.DBSchemaObject;

import java.util.List;

public interface DBIndex extends DBSchemaObject {
    boolean isUnique();
    DBDataset getDataset();

    List<DBColumn> getColumns();
}
