package com.dci.intellij.dbn.diagnostics.ui.model;

import com.dci.intellij.dbn.common.ui.table.DBNReadonlyTableModel;
import com.dci.intellij.dbn.diagnostics.data.DiagnosticEntry;
import com.dci.intellij.dbn.diagnostics.data.ParserDiagnosticsDeltaResult;
import com.dci.intellij.dbn.diagnostics.data.ParserDiagnosticsEntry;
import com.dci.intellij.dbn.diagnostics.data.ParserDiagnosticsFilter;
import com.intellij.openapi.Disposable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParserDiagnosticsTableModel implements DBNReadonlyTableModel<ParserDiagnosticsEntry>, Disposable {
    public static final ParserDiagnosticsTableModel EMPTY = new ParserDiagnosticsTableModel(null, null);
    public static final String[] INITIAL_COLUMNS = {"#", "File", "Error Count", "Warning Count"};
    public static final String[] DELTA_COLUMNS = {"#", "File", "Previous Errors", "Previous Warnings", "Errors", "Warnings", "Transition"};

    private final ParserDiagnosticsDeltaResult deltaResult;

    public ParserDiagnosticsTableModel(@Nullable ParserDiagnosticsDeltaResult deltaResult, @Nullable ParserDiagnosticsFilter filter) {
        this.deltaResult = deltaResult;
        if (this.deltaResult != null) {
            this.deltaResult.setFilter(filter);
        }
    }

    public ParserDiagnosticsDeltaResult getResult() {
        return deltaResult;
    }

    @NotNull
    protected String[] getColumnNames() {
        return isInitial() ?
                INITIAL_COLUMNS :
                DELTA_COLUMNS;
    }

    private boolean isInitial() {
        return deltaResult == null || deltaResult.getPrevious() == null;
    }

    @Override
    public final int getRowCount() {
        return deltaResult == null ? 0 : deltaResult.getEntries().size();
    }

    @Override
    public final int getColumnCount() {
        return getColumnNames().length;
    }

    @Override
    public final String getColumnName(int columnIndex) {
        return getColumnNames()[columnIndex];
    }

    @Override
    public final Class<?> getColumnClass(int columnIndex) {
        return DiagnosticEntry.class;
    }

    @Override
    public final Object getValueAt(int rowIndex, int columnIndex) {
        return deltaResult == null ? null : deltaResult.getEntries().get(rowIndex);
    }

    @Override
    public Object getValue(ParserDiagnosticsEntry row, int column) {
        if (isInitial()) {
            switch (column) {
                case 0: return getRowIndex(row);
                case 1: return row.getFile();
                case 2: return row.getNewIssues().getErrors();
                case 3: return row.getNewIssues().getWarnings();
            }
        } else {
            switch (column) {
                case 0: return getRowIndex(row);
                case 1: return row.getFile();
                case 2: return row.getOldIssues().getErrors();
                case 3: return row.getOldIssues().getWarnings();
                case 4: return row.getNewIssues().getErrors();
                case 5: return row.getNewIssues().getWarnings();
                case 6: return row.getStateTransition();
            }
        }
        return "";
    }

    private int getRowIndex(ParserDiagnosticsEntry row) {
        return deltaResult.getEntries().indexOf(row);
    }

    @Override
    public String getPresentableValue(ParserDiagnosticsEntry row, int column) {
        if (isInitial()) {
            switch (column) {
                case 0: return Integer.toString(getRowIndex(row));
                case 1: return row.getFilePath();
                case 2: return Integer.toString(row.getNewIssues().getErrors());
                case 3: return Integer.toString(row.getNewIssues().getWarnings());
            }
        } else {
            switch (column) {
                case 0: return Integer.toString(getRowIndex(row));
                case 1: return row.getFilePath();
                case 2: return Integer.toString(row.getOldIssues().getErrors());
                case 3: return Integer.toString(row.getOldIssues().getWarnings());
                case 4: return Integer.toString(row.getNewIssues().getErrors());
                case 5: return Integer.toString(row.getNewIssues().getWarnings());
                case 6: return row.getStateTransition().name();
            }
        }
        return "";
    }



    @Getter
    @Setter
    private boolean disposed;

    @Override
    public void disposeInner() {

    }
}
