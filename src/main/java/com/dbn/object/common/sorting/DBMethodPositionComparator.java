package com.dbn.object.common.sorting;

import com.dbn.object.DBMethod;
import com.dbn.object.DBProgram;
import com.dbn.object.type.DBObjectType;

public abstract class DBMethodPositionComparator<T extends DBMethod> extends DBObjectComparator<T> {
    public DBMethodPositionComparator(DBObjectType objectType) {
        super(objectType, SortingType.POSITION);
    }

    @Override
    public int compare(DBMethod method1, DBMethod method2) {
        DBProgram program1 = method1.getProgram();
        DBProgram program2 = method2.getProgram();
        if (program1 != null && program2 != null) {
            int result = compareRef(program1, program2);
            if (result == 0) {
                return comparePosition(method1, method2);
            }

            return result;
        } else {
            int result = comparePosition(method1, method2);
            if (result == 0) {
                result = compareName(method1, method2);
                if (result == 0) {
                    return compareOverload(method1, method2);
                }
            }

            return result;
        }
    }
}
