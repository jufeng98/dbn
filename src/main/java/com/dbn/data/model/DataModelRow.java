package com.dbn.data.model;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.dispose.UnlistedDisposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DataModelRow<
        M extends DataModel<? extends DataModelRow<M, C>, C>,
        C extends DataModelCell<? extends DataModelRow<M, C>, M>>
        extends StatefulDisposable, UnlistedDisposable {

    List<C> getCells();

    @Nullable
    C getCell(String columnName);

    @Nullable
    Object getCellValue(String columnName);

    @Nullable
    C getCellAtIndex(int index);

    int getIndex();

    void setIndex(int index);

    @NotNull
    M getModel();
}
