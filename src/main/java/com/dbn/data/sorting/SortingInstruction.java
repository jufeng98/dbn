package com.dbn.data.sorting;

import com.dbn.common.util.Cloneable;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SortingInstruction implements Cloneable<SortingInstruction> {
    private int index;
    private String columnName;
    private SortDirection direction;

    public SortingInstruction(String columnName, SortDirection direction) {
        this.columnName = columnName.intern();
        this.direction = direction;
    }

    public void switchDirection() {
        if (direction == SortDirection.ASCENDING) {
            direction = SortDirection.DESCENDING;
        } else if (direction == SortDirection.DESCENDING) {
            direction = SortDirection.ASCENDING;
        }
    }

    @Override
    public SortingInstruction clone() {
        return new SortingInstruction(columnName, direction);
    }

    public DBColumn getColumn(DBDataset dataset) {
        return dataset.getColumn(columnName);
    }
}
