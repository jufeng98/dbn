package com.dbn.object.common.sorting;

import com.dbn.object.DBArgument;
import com.dbn.object.DBMethod;
import com.dbn.object.type.DBObjectType;

public class DBArgumentPositionComparator extends DBObjectComparator<DBArgument> {
    public DBArgumentPositionComparator() {
        super(DBObjectType.ARGUMENT, SortingType.POSITION);
    }

    @Override
    public int compare(DBArgument argument1, DBArgument argument2) {
        DBMethod method1 = argument1.getMethod();
        DBMethod method2 = argument2.getMethod();
        int result = compareRef(method1, method2);
        if (result == 0) {
            return comparePosition(argument1, argument2);
        }
        return result;
    }
}
