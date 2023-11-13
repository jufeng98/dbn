package com.dbn.common.ui.table;

import javax.swing.event.TableModelListener;

public interface DBNReadonlyTableModel<R> extends DBNTableModel<R> {
    default boolean isReadonly(){return true;}
    default boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    default void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
    default void addTableModelListener(TableModelListener l) {}
    default void removeTableModelListener(TableModelListener l) {}

}
