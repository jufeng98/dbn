package com.dbn.object.common.sorting;

import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.type.DBObjectType;

public class DBColumnPositionComparator extends DBObjectComparator<DBColumn> {

    public DBColumnPositionComparator() {
        super(DBObjectType.COLUMN, SortingType.POSITION);
    }

    @Override
    public int compare(DBColumn column1, DBColumn column2) {
        DBDataset dataset1 = column1.getDataset();
        DBDataset dataset2 = column2.getDataset();
        int result = compareRef(dataset1, dataset2);
        if (result == 0) {
            return comparePosition(column1, column2);
        }
        return result;
    }
}
