package com.dci.intellij.dbn.common.ui.table;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;
import com.dci.intellij.dbn.common.exception.OutdatedContentException;

import javax.swing.table.TableModel;

public interface DBNTableModel<R> extends TableModel, StatefulDisposable {
    default String getPresentableValue(R rowObject, int column) {
        return rowObject == null ? "" : rowObject.toString();
    };

    default Object getValue(R rowObject, int column) {
        throw new UnsupportedOperationException();
    };

    default void checkRowBounds(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) throw new OutdatedContentException(this);
    }

    default void checkColumnBounds(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) throw new OutdatedContentException(this);
    }
}
