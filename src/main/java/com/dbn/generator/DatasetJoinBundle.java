package com.dbn.generator;

import com.dbn.object.DBDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatasetJoinBundle {
    private final List<DatasetJoin> joins = new ArrayList<>();

    public DatasetJoinBundle(Set<DBDataset> datasets, boolean lenient) {
        for (DBDataset dataset1 : datasets) {
            for (DBDataset dataset2 : datasets) {
                if (!dataset1.equals(dataset2)) {
                    createJoin(dataset1, dataset2, lenient);
                }
            }
        }
    }

    private void createJoin(DBDataset dataset1, DBDataset dataset2, boolean lenient) {
        if (!contains(dataset1, dataset2)) {
            DatasetJoin datasetJoin = new DatasetJoin(dataset1, dataset2, lenient);
            if (!datasetJoin.isEmpty()) {
                joins.add(datasetJoin);
            }
        }
    }

    protected boolean contains(DBDataset... datasets) {
        for (DatasetJoin datasetJoin : joins) {
            if (datasetJoin.contains(datasets)) return true;
        }
        return false;
    }

    public List<DatasetJoin> getJoins() {
        return joins;
    }

    public boolean isEmpty() {
        for (DatasetJoin join : joins) {
            if (!join.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
