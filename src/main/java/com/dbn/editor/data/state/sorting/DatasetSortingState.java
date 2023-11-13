package com.dbn.editor.data.state.sorting;

import com.dbn.data.sorting.SortingState;
import com.dbn.object.DBDataset;
import com.dbn.object.lookup.DBObjectRef;

public class DatasetSortingState extends SortingState {
    private final DBObjectRef<DBDataset> datasetRef;

    public DatasetSortingState(DBDataset dataset) {
        this.datasetRef = DBObjectRef.of(dataset);
    }

    public DBDataset getDataset() {
        return DBObjectRef.get(datasetRef);
    }


}
