package com.dbn.data.model.sortable;

import com.dbn.data.model.DataModelCell;
import com.dbn.data.model.basic.BasicDataModelCell;
import org.jetbrains.annotations.NotNull;

public class SortableDataModelCell<
        R extends SortableDataModelRow<M, ? extends SortableDataModelCell<R, M>>,
        M extends SortableDataModel<R, ? extends SortableDataModelCell<R, M>>>
        extends BasicDataModelCell<R, M>
        implements Comparable<DataModelCell> {

    public SortableDataModelCell(R row, Object userValue, int index) {
        super(userValue, row, index);
    }

    @NotNull
    @Override
    public M getModel() {
        return super.getModel();
    }

    @NotNull
    @Override
    public R getRow() {
        return super.getRow();
    }

    @Override
    public int compareTo(@NotNull DataModelCell cell) {
        Object local = getUserValue();
        Object remote = cell.getUserValue();

        boolean nullsFirst = getModel().isSortingNullsFirst();

        if (local == null && remote == null) return 0;
        if (local == null) return nullsFirst ? -1 : 1;
        if (remote == null) return nullsFirst ? 1 : -1;
        // local class may differ from remote class for
        // columns with data conversion error
        Class<?> localClass = local.getClass();
        Class<?> remoteClass = remote.getClass();

        if (local instanceof Comparable && remote instanceof Comparable && localClass.equals(remoteClass)) {
            Comparable localComparable = (Comparable) local;
            Comparable remoteComparable = (Comparable) remote;
            return localComparable.compareTo(remoteComparable);
        } else {
            Class typeClass = cell.getColumnInfo().getDataType().getTypeClass();
            return localClass.equals(typeClass) ? 1 :
                   remoteClass.equals(typeClass) ? -1 : 0;
        }
    }
}
