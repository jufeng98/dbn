package com.dci.intellij.dbn.common.ui.table;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.latent.Latent;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.HashSet;
import java.util.Set;

public abstract class DBNEditableTableModel extends StatefulDisposableBase implements DBNTableWithGutterModel {
    private final Set<TableModelListener> tableModelListeners = new HashSet<>();
    private final Latent<DBNTableGutterModel<?>> listModel = Latent.basic(() -> new DBNTableGutterModel<>(DBNEditableTableModel.this));

    @Override
    public void addTableModelListener(TableModelListener listener) {
        tableModelListeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        tableModelListeners.remove(listener);
    }

    @Override
    public ListModel<?> getListModel() {
        return listModel.get();
    }

    public abstract void insertRow(int rowIndex);

    public abstract void removeRow(int rowIndex);

    public void notifyListeners(int firstRowIndex, int lastRowIndex, int columnIndex) {
        TableModelEvent modelEvent = new TableModelEvent(this, firstRowIndex, lastRowIndex, columnIndex);
        for (TableModelListener listener : tableModelListeners) {
            listener.tableChanged(modelEvent);
        }

        if (listModel.loaded()) {
            ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, firstRowIndex, lastRowIndex);
            listModel.get().notifyListeners(listDataEvent);
        }
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
