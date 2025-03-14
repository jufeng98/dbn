package com.dbn.execution.statement.result.ui;

import com.dbn.common.action.DataKeys;
import com.dbn.common.action.DataProviders;
import com.dbn.common.color.Colors;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.icon.Icons;
import com.dbn.common.latent.Latent;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.misc.DBNTableScrollPane;
import com.dbn.common.ui.util.Borders;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.common.util.Actions;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SessionId;
import com.dbn.data.find.DataSearchComponent;
import com.dbn.data.find.SearchableDataComponent;
import com.dbn.data.grid.ui.table.basic.BasicTable;
import com.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import com.dbn.data.model.resultSet.ResultSetDataModel;
import com.dbn.data.record.RecordViewInfo;
import com.dbn.editor.data.DatasetEditor;
import com.dbn.execution.ExecutionManager;
import com.dbn.execution.common.result.ui.ExecutionResultFormBase;
import com.dbn.execution.statement.result.StatementExecutionCursorResult;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.dbn.editor.data.action.DataReloadAction.LOAD_INSTRUCTIONS;

public class StatementExecutionResultForm extends ExecutionResultFormBase<StatementExecutionCursorResult> implements SearchableDataComponent {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel statusPanel;
    private JPanel searchPanel;
    private JPanel resultPanel;
    private JLabel statusLabel;
    private ResultSetTable<?> resultTable;
    private DBNTableScrollPane resultScrollPane;
    private final RecordViewInfo recordViewInfo;
    private JComponent datasetEditorComponent;

    private transient final Latent<DataSearchComponent> dataSearchComponent = Latent.basic(() -> {
        DataSearchComponent dataSearchComponent = new DataSearchComponent(StatementExecutionResultForm.this);
        searchPanel.add(dataSearchComponent.getComponent(), BorderLayout.CENTER);
        DataProviders.register(dataSearchComponent.getSearchField(), this);
        return dataSearchComponent;
    });

    public StatementExecutionResultForm(@NotNull StatementExecutionCursorResult executionResult, @Nullable DatasetEditor datasetEditor) {
        super(executionResult);

        if (datasetEditor != null) {
            recordViewInfo = new RecordViewInfo(executionResult.getName(), executionResult.getIcon());
            datasetEditorComponent = datasetEditor.getComponent();

            datasetEditorComponent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() != MouseEvent.BUTTON3) {
                        return;
                    }

                    ActionGroup actionGroup = new DefaultActionGroup(new AnAction("Reload", "Reload data",
                            Icons.DATA_EDITOR_RELOAD_DATA) {
                        @Override
                        public void actionPerformed(@NotNull AnActionEvent e) {
                            datasetEditor.loadData(LOAD_INSTRUCTIONS);
                        }
                    });
                    ActionPopupMenu actionPopupMenu = Actions.createActionPopupMenu(datasetEditorComponent, "", actionGroup);
                    JPopupMenu popupMenu = actionPopupMenu.getComponent();
                    popupMenu.show(datasetEditorComponent, e.getX(), e.getY());
                }
            });
            return;
        }

        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.StatementExecutionResult", "", false);

        actionsPanel.add(actionToolbar.getComponent());

        recordViewInfo = new RecordViewInfo(executionResult.getName(), executionResult.getIcon());

        resultPanel.setBorder(Borders.lineBorder(JBColor.border(), 0, 1, 1, 0));
        resultTable = new ResultSetTable<>(this, executionResult.getTableModel(), true, recordViewInfo);
        resultTable.setName(executionResult.getName());

        resultScrollPane.setViewportView(resultTable);
        resultTable.initTableGutter();

        Disposer.register(this, resultTable);
        Disposer.register(this, executionResult);
    }

    public void rebuildForm() {
        Dispatch.run(() -> {
            StatementExecutionCursorResult executionResult = getExecutionResult();
            JScrollBar horizontalScrollBar = resultScrollPane.getHorizontalScrollBar();
            int horizontalScrolling = horizontalScrollBar.getValue();
            ResultSetTable<?> newResultSetTable = new ResultSetTable<>(this, executionResult.getTableModel(), true, recordViewInfo);
            resultTable = Disposer.replace(resultTable, newResultSetTable);
            resultScrollPane.setViewportView(resultTable);
            resultTable.setBackground(Colors.getEditorBackground());
            resultTable.initTableGutter();
            resultTable.setName(getExecutionResult().getName());
            horizontalScrollBar.setValue(horizontalScrolling);
        });
    }

    @NotNull
    public ResultSetTable<?> getResultTable() {
        return Failsafe.nn(resultTable);
    }

    public void updateVisibleComponents() {
        Dispatch.run(() -> {
            StatementExecutionCursorResult executionResult = getExecutionResult();
            ResultSetDataModel<?, ?> dataModel = executionResult.getTableModel();
            ConnectionHandler connection = executionResult.getConnection();
            String connectionName = connection.getName();
            SessionId sessionId = executionResult.getExecutionInput().getTargetSessionId();
            String connectionType =
                    sessionId == SessionId.MAIN ? " (main)" :
                    sessionId == SessionId.POOL ? " (pool)" : " (session)";
            int rowCount = dataModel.getRowCount();
            String partialResultInfo = dataModel.isResultSetExhausted() ? "" : " (partial)";
            long executeDuration = dataModel.getExecuteDuration();
            long fetchDuration = dataModel.getFetchDuration();

            String executionDurationInfo = executeDuration == -1 ? "" : " - executed in " + executeDuration + " ms.";
            String fetchDurationInfo = fetchDuration == -1 ? "" : " / fetched in " + fetchDuration + " ms.";

            statusLabel.setText(connectionName + connectionType + ": " + rowCount + " records " + partialResultInfo + executionDurationInfo + fetchDurationInfo );
            statusLabel.setIcon(connection.getIcon());
        });
    }

    public void show() {
        StatementExecutionCursorResult executionResult = getExecutionResult();
        Project project = executionResult.getProject();
        ExecutionManager.getInstance(project).selectResultTab(executionResult);
    }

    @NotNull
    @Override
    public JComponent getMainComponent() {
        if (datasetEditorComponent != null) {
            return datasetEditorComponent;
        }

        return mainPanel;
    }

    public void highlightLoading(boolean loading) {
        ResultSetTable<?> resultTable = getResultTable();
        resultTable.setLoading(loading);
        UserInterface.repaint(resultTable);
    }

    /*********************************************************
     *              SearchableDataComponent                  *
     *********************************************************/
    @Override
    public void showSearchHeader() {
        getResultTable().clearSelection();

        DataSearchComponent dataSearchComponent = getSearchComponent();
        dataSearchComponent.initializeFindModel();
        if (searchPanel.isVisible()) {
            dataSearchComponent.getSearchField().selectAll();
        } else {
            searchPanel.setVisible(true);
        }
        dataSearchComponent.getSearchField().requestFocus();

    }

    private DataSearchComponent getSearchComponent() {
        return dataSearchComponent.get();
    }

    @Override
    public void hideSearchHeader() {
        getSearchComponent().resetFindModel();
        searchPanel.setVisible(false);
        UserInterface.repaintAndFocus(getResultTable());
    }

    @Override
    public void cancelEditActions() {
    }

    @NotNull
    @Override
    public BasicTable getTable() {
        return getResultTable();
    }

    @Override
    public String getSelectedText() {
        return null;
    }

    /********************************************************
     *                    Data Provider                     *
     ********************************************************/
    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        if (DataKeys.STATEMENT_EXECUTION_CURSOR_RESULT.is(dataId)) return getExecutionResult();
        return null;
    }
}
