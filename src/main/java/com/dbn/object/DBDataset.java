package com.dbn.object;

import com.dbn.object.common.DBSchemaObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DBDataset extends DBSchemaObject {
    @NotNull List<DBColumn> getColumns();
    @Nullable DBColumn getColumn(String name);
    @NotNull String getComment();
    @Nullable List<DBConstraint> getConstraints();
    @Nullable DBConstraint getConstraint(String name);

    @Nullable List<DBDatasetTrigger> getTriggers();
    @Nullable DBDatasetTrigger getTrigger(String name);

    @Nullable List<DBIndex> getIndexes();
    @Nullable DBIndex getIndex(String name);

    boolean hasLobColumns();
}