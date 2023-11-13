package com.dbn.data.model;

import com.dbn.common.filter.Filter;
import com.dbn.common.ui.table.DBNTableWithGutterModel;
import com.dbn.data.find.DataSearchResult;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DataModel<
        R extends DataModelRow<? extends DataModel<R, C>, C>,
        C extends DataModelCell<R, ? extends DataModel<R, C>>>
        extends DBNTableWithGutterModel {

    boolean isReadonly();

    @NotNull
    Project getProject();

    void setFilter(Filter<R> filter);

    @Nullable
    Filter<R> getFilter();

    @NotNull
    List<R> getRows();

    int indexOfRow(R row);

    @Nullable
    R getRowAtIndex(int index);

    DataModelHeader getHeader();

    ColumnInfo getColumnInfo(int columnIndex);

    @NotNull
    DataModelState getState();

    void setState(DataModelState state);

    DataSearchResult getSearchResult();

    void addDataModelListener(DataModelListener listener);

    void removeDataModelListener(DataModelListener listener);

    boolean hasSearchResult();

    int getColumnIndex(String columnName);
}
