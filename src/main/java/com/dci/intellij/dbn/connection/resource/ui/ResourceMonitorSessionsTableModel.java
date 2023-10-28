package com.dci.intellij.dbn.connection.resource.ui;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.ui.table.DBNReadonlyTableModel;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResourceMonitorSessionsTableModel extends StatefulDisposableBase implements DBNReadonlyTableModel {
    private final ConnectionRef connection;
    private final List<DatabaseSession> sessions;

    ResourceMonitorSessionsTableModel(ConnectionHandler connection) {
        this.connection = connection.ref();
        sessions = connection.getSessionBundle().getSessions();
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @NotNull
    public Project getProject() {
        return getConnection().getProject();
    }

    @NotNull
    public List<DatabaseSession> getSessions() {
        return Failsafe.nn(sessions);
    }

    @Override
    public int getRowCount() {
        return getSessions().size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Session";
            case 1: return "Status";
            case 2: return "Last Access";
            case 3: return "Open Connections / Peak";
            case 4: return "Open Cursors";
            case 5: return "Cached Statements";
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return DatabaseSession.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getSession(rowIndex);
    }

    @Nullable
    public DatabaseSession getSession(int rowIndex) {
        return rowIndex == -1 ? null : sessions.get(rowIndex);
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
