package com.dbn.editor.data.filter;

import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.lookup.DBObjectRef;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatasetFilterInput {
    private final DBObjectRef<DBDataset> dataset;
    private final Map<DBObjectRef<DBColumn>, Object> values = new LinkedHashMap<>();

    public DatasetFilterInput(DBDataset dataset) {
        this.dataset = DBObjectRef.of(dataset);
    }

    public DBDataset getDataset() {
        return dataset.get();
    }

    public List<DBColumn> getColumns() {
        return values.keySet().stream().map(ref -> ref.get()).collect(Collectors.toList());
    }

    public void setColumnValue(@NotNull DBColumn column, Object value) {
        values.put(DBObjectRef.of(column), value);
    }

    public Object getColumnValue(DBColumn column) {
        return values.get(DBObjectRef.of(column));
    }
}
