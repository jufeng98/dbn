package com.dci.intellij.dbn.common.ui.table;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.ui.util.Listeners;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class DBNTableGutterModel<T extends DBNTableWithGutterModel> extends StatefulDisposableBase implements ListModel {
    private final WeakRef<T> tableModel;
    private final Listeners<ListDataListener> listeners = Listeners.create(this);

    public DBNTableGutterModel(@NotNull T tableModel) {
        this.tableModel = WeakRef.of(tableModel);

        Disposer.register(tableModel, this);
    }

    @NotNull
    public T getTableModel() {
        return tableModel.ensure();
    }

    @Override
    public int getSize() {
        return getTableModel().getRowCount();
    }

    @Override
    public Object getElementAt(int index) {
        return index + 1;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void notifyListeners(ListDataEvent e) {
        listeners.notify(l -> l.contentsChanged(e));
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
