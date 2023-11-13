package com.dbn.object.common.sorting;

import com.dbn.object.DBProcedure;
import com.dbn.object.type.DBObjectType;

public class DBFunctionNameComparator extends DBMethodNameComparator<DBProcedure> {
    public DBFunctionNameComparator() {
        super(DBObjectType.FUNCTION);
    }
}
