package com.dbn.diagnostics.ui.model;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.diagnostics.DiagnosticsManager;
import com.dbn.diagnostics.data.DiagnosticBundle;
import com.dbn.diagnostics.data.DiagnosticEntry;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MetadataDiagnosticsTableModel extends AbstractDiagnosticsTableModel<String> {
    private final ConnectionRef connection;

    private static final String[] COLUMN_NAMES = new String[] {
            "Identifier",
            "Invocations",
            "Failures",
            "Timeouts",
            "Best Query (ms)",
            "Best Load (ms)",
            "Worst Query (ms)",
            "Worst Load (ms)",
            "Average Query (ms)",
            "Average Load (ms)",
            "Total Query (ms)",
            "Total Load (ms)",
            "Fetch Block Size"};

    public MetadataDiagnosticsTableModel(ConnectionHandler connection) {
        super(connection.getProject());
        this.connection = connection.ref();
    }

    @NotNull
    @Override
    protected String[] getColumnNames() {
        return COLUMN_NAMES;
    }

    @NotNull
    @Override
    protected DiagnosticBundle<String> resolveDiagnostics() {
        DiagnosticsManager diagnosticsManager = DiagnosticsManager.getInstance(getProject());
        return diagnosticsManager.getMetadataInterfaceDiagnostics(connection.getConnectionId());
    }

    @Override
    public Object getValue(DiagnosticEntry<String> entry, int column) {
        DiagnosticEntry<String> q = entry.getDetail("QUERY");
        DiagnosticEntry<String> l = entry.getDetail("LOAD");
        switch (column) {
            case 0: return q.getIdentifier();
            case 1: return q.getInvocations();
            case 2: return q.getFailures();
            case 3: return q.getTimeouts();
            case 4: return q.getBest();
            case 5: return l.getBest();
            case 6: return q.getWorst();
            case 7: return l.getWorst();
            case 8: return q.getAverage();
            case 9: return l.getAverage();
            case 10: return q.getTotal();
            case 11: return l.getTotal();
            case 12: return entry.getDetail("FETCH_BLOCK").getAverage();
        }
        return "";
    }

    @Override
    public String getPresentableValue(DiagnosticEntry<String> entry, int column) {
        return getValue(entry, column).toString();
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @NotNull
    public Project getProject() {
        return getConnection().getProject();
    }
}
