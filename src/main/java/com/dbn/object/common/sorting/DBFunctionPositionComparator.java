package com.dbn.object.common.sorting;

import com.dbn.object.DBProcedure;
import com.dbn.object.type.DBObjectType;

public class DBFunctionPositionComparator extends DBMethodPositionComparator<DBProcedure> {
    public DBFunctionPositionComparator() {
        super(DBObjectType.FUNCTION);
    }
}
