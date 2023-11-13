package com.dbn.object.common.sorting;

import com.dbn.object.DBMethod;
import com.dbn.object.DBProgram;
import com.dbn.object.type.DBObjectType;

public abstract class DBMethodNameComparator<T extends DBMethod> extends DBObjectComparator<T> {
    public DBMethodNameComparator(DBObjectType objectType) {
        super(objectType, SortingType.NAME);
    }

    @Override
    public int compare(DBMethod method1, DBMethod method2) {
        DBProgram program1 = method1.getProgram();
        DBProgram program2 = method2.getProgram();

        int result = compareRef(program1, program2);
        if (result == 0) {
            result = compareName(method1, method2);
            if (result == 0) {
                return compareOverload(method1, method2);
            }
        }

        return result;
    }
}
