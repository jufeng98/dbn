package com.dbn.object.common.sorting;

import com.dbn.object.DBArgument;
import com.dbn.object.DBFunction;
import com.dbn.object.DBMethod;
import com.dbn.object.type.DBObjectType;

public class DBArgumentNameComparator extends DBObjectComparator<DBArgument> {
    public DBArgumentNameComparator() {
        super(DBObjectType.ARGUMENT, SortingType.NAME);
    }

    @Override
    public int compare(DBArgument argument1, DBArgument argument2) {
        DBMethod method1 = argument1.getMethod();
        DBMethod method2 = argument2.getMethod();
        int result = compareRef(method1, method2);
        if (result == 0) {
            if (method1 instanceof DBFunction) {
                if (method1.getPosition() == 1) {
                    return -1;
                }
                if (method2.getPosition() == 1) {
                    return 1;
                }
            }
            return compareName(argument1, argument2);
        }

        return result;
    }
}
