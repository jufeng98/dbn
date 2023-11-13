package com.dbn.object.common.sorting;

import com.dbn.object.DBType;
import com.dbn.object.DBTypeAttribute;
import com.dbn.object.type.DBObjectType;

public class DBTypeAttributePositionComparator extends DBObjectComparator<DBTypeAttribute> {
    public DBTypeAttributePositionComparator() {
        super(DBObjectType.TYPE_ATTRIBUTE, SortingType.POSITION);
    }

    @Override
    public int compare(DBTypeAttribute attribute1, DBTypeAttribute attribute2) {
        DBType type1 = attribute1.getType();
        DBType type2 = attribute2.getType();
        int result = compareRef(type1, type2);
        if (result == 0) {
            return comparePosition(attribute1, attribute2);
        }
        return result;
    }
}
