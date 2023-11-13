package com.dbn.object;

import com.dbn.object.common.DBObject;

public interface DBNestedTableColumn extends DBObject {
    DBObject getNestedTable();
}