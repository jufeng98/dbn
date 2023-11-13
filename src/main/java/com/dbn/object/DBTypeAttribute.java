package com.dbn.object;

import com.dbn.data.type.DBDataType;

public interface DBTypeAttribute extends DBOrderedObject {
    DBType getType();
    DBDataType getDataType();
}
