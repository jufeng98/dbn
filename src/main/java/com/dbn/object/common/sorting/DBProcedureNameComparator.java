package com.dbn.object.common.sorting;

import com.dbn.object.DBProcedure;
import com.dbn.object.type.DBObjectType;

public class DBProcedureNameComparator extends DBMethodNameComparator<DBProcedure> {
    public DBProcedureNameComparator() {
        super(DBObjectType.PROCEDURE);
    }
}
