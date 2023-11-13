package com.dbn.data.model.sortable;

import com.dbn.data.sorting.SortingInstruction;
import com.dbn.data.sorting.SortingState;
import com.dbn.data.model.basic.BasicDataModelRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SortableDataModelRow<
        M extends SortableDataModel<? extends SortableDataModelRow<M, C>, C>,
        C extends SortableDataModelCell<? extends SortableDataModelRow<M, C>, M>>
        extends BasicDataModelRow<M, C>
        implements Comparable {

    protected SortableDataModelRow(M model) {
        super(model);
    }

    @NotNull
    @Override
    public M getModel() {
        return super.getModel();
    }

    @Nullable
    @Override
    public C getCellAtIndex(int index) {
        return super.getCellAtIndex(index);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        SortableDataModelRow row = (SortableDataModelRow) o;
        SortableDataModel model = getModel();
        SortingState sortingState = model.getSortingState();

        for (SortingInstruction sortingInstruction : sortingState.getInstructions()) {
            int columnIndex = model.getColumnIndex(sortingInstruction.getColumnName());

            if (columnIndex > -1) {
                SortableDataModelCell local = getCellAtIndex(columnIndex);
                SortableDataModelCell remote = row.getCellAtIndex(columnIndex);

                int compareIndex = sortingInstruction.getDirection().getCompareAdj();

                int result =
                        remote == null && local == null ? 0 :
                        local == null ? -compareIndex :
                        remote == null ? columnIndex :
                                compareIndex * local.compareTo(remote);

                if (result != 0) return result;
            }
        }
        return 0;


/*
        int index = model.getSortColumnIndex();

        if (index == -1) return 0;
        SortableDataModelRow row = (SortableDataModelRow) o;

        SortableDataModelCell local = getCellAtIndex(index);
        SortableDataModelCell remote = row.getCellAtIndex(index);

        int compareIndex = model.getSortDirection().getCompareIndex();

        if (remote == null && local == null) return 0;
        if (local == null) return -compareIndex;
        if (remote == null) return compareIndex;

        return compareIndex * local.compareTo(remote);
*/
    }

}
