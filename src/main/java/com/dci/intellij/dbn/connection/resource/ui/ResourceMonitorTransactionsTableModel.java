package com.dci.intellij.dbn.connection.resource.ui;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.ui.table.DBNReadonlyTableModel;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.transaction.PendingTransaction;
import com.dci.intellij.dbn.connection.transaction.PendingTransactionBundle;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class ResourceMonitorTransactionsTableModel extends StatefulDisposableBase implements DBNReadonlyTableModel, Disposable {
    private final ConnectionRef connection;
    private DBNConnection conn;

    public ResourceMonitorTransactionsTableModel(ConnectionHandler connection, @Nullable DBNConnection conn) {
        this.connection = connection.ref();
        this.conn = conn;
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @NotNull
    public Project getProject() {
        return getConnection().getProject();
    }

    @Override
    public int getRowCount() {
        PendingTransactionBundle dataChanges = conn == null ? null : conn.getDataChanges();
        return dataChanges == null ? 0 : dataChanges.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return
            columnIndex == 0 ? "Source" :
            columnIndex == 1 ? "Details" : null ;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return PendingTransaction.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PendingTransactionBundle dataChanges = conn == null ? null : conn.getDataChanges();
        if (dataChanges != null) {
            return dataChanges.getEntries().get(rowIndex);
        }
        return null;
    }

    @Override
    public void disposeInner() {
        conn = null;
    }
}
