package com.dci.intellij.dbn.common.ui.table;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.ui.util.Listeners;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Mutable model, not really editable
 * @param <R>
 */
public abstract class DBNMutableTableModel<R> extends StatefulDisposableBase implements DBNTableModel<R> {
    private final Listeners<TableModelListener> listeners = Listeners.create(this);
    public boolean isReadonly(){
        return true;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public final void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public final void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    public final void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    public final void notifyRowChange(int row) {
        TableModelEvent event = new TableModelEvent(this, row);
        listeners.notify(l -> l.tableChanged(event));
    }

    public final void notifyRowChanges() {
        TableModelEvent event = new TableModelEvent(this);
        listeners.notify(l -> l.tableChanged(event));
    }
}
