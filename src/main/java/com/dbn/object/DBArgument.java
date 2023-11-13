package com.dbn.object;

import com.dbn.data.type.DBDataType;

public interface DBArgument extends DBOrderedObject {
    DBDataType getDataType();
    DBMethod getMethod();
    short getSequence();
    boolean isInput();
    boolean isOutput();
}