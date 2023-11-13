package com.dbn.editor.data.filter;

import com.dbn.common.options.PersistentConfiguration;
import com.dbn.connection.ConnectionId;
import com.dbn.data.sorting.SortingState;
import com.dbn.object.DBDataset;
import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public interface DatasetFilter extends UnnamedConfigurable, PersistentConfiguration {
    Icon getIcon();
    @NotNull
    String getId();
    String getName();
    String getVolatileName();
    ConnectionId getConnectionId();
    String getDatasetName();
    boolean isPersisted();
    boolean isTemporary();
    boolean isIgnored();
    DatasetFilterType getFilterType();

    String getError();
    void setError(String error);

    DatasetFilterGroup getFilterGroup() ;

    String createSelectStatement(DBDataset dataset, SortingState sortingState);
}
