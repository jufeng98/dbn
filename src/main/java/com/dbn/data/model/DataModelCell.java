package com.dbn.data.model;

import com.dbn.common.dispose.UnlistedDisposable;
import com.dbn.data.editor.ui.UserValueHolder;
import com.dbn.data.value.LargeObjectValue;
import org.jetbrains.annotations.NotNull;

public interface DataModelCell<
        R extends DataModelRow<M, ? extends DataModelCell<?, ?>>,
        M extends DataModel<R, ? extends DataModelCell<?, ?>>>
        extends UnlistedDisposable, UserValueHolder<Object> {

    ColumnInfo getColumnInfo();

    int getIndex();

    @NotNull
    M getModel();

    @NotNull
    R getRow();

    default boolean isLargeValue() {
        return getUserValue() instanceof LargeObjectValue;
    }

    default String getTemporaryUserValue() {
        return null;
    }
}
