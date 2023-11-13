package com.dbn.object.common.sorting;

import com.dbn.object.DBProcedure;
import com.dbn.object.type.DBObjectType;

public class DBProcedurePositionComparator extends DBMethodPositionComparator<DBProcedure> {
    public DBProcedurePositionComparator() {
        super(DBObjectType.PROCEDURE);
    }
}
