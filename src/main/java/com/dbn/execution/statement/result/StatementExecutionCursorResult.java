package com.dbn.execution.statement.result;

import com.dbn.common.action.DataKeys;
import com.dbn.common.dispose.Checks;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.thread.Progress;
import com.dbn.common.util.Messages;
import com.dbn.connection.ConnectionAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.connection.jdbc.DBNResultSet;
import com.dbn.connection.jdbc.DBNStatement;
import com.dbn.connection.mapping.FileConnectionContextManager;
import com.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import com.dbn.data.model.resultSet.ResultSetDataModel;
import com.dbn.editor.DBContentType;
import com.dbn.editor.data.DatasetEditor;
import com.dbn.editor.data.DatasetEditorManager;
import com.dbn.editor.data.filter.DatasetCustomFilter;
import com.dbn.editor.data.filter.DatasetFilter;
import com.dbn.editor.data.filter.DatasetFilterGroup;
import com.dbn.editor.data.filter.DatasetFilterManager;
import com.dbn.editor.data.filter.DatasetFilterUtil;
import com.dbn.execution.ExecutionStatus;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.execution.statement.options.StatementExecutionSettings;
import com.dbn.execution.statement.processor.StatementExecutionCursorProcessor;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.execution.statement.result.ui.StatementExecutionResultForm;
import com.dbn.execution.statement.StatementExecutionContext;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.language.common.psi.ExecutablePsiElement;
import com.dbn.object.DBDataset;
import com.dbn.object.DBSchema;
import com.dbn.object.DBTable;
import com.dbn.object.common.DBObjectBundle;
import com.dbn.sql.gutter.MockExecutablePsiElement;
import com.dbn.utils.NotifyUtil;
import com.dbn.vfs.DatabaseFileSystem;
import com.dbn.vfs.file.DBDatasetVirtualFile;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dbn.common.dispose.Failsafe.nd;
import static com.dbn.common.dispose.Failsafe.nn;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class StatementExecutionCursorResult extends StatementExecutionBasicResult {
    private ResultSetDataModel<?, ?> dataModel;
    private DatasetEditor datasetEditor;

    public StatementExecutionCursorResult(
            @NotNull StatementExecutionProcessor executionProcessor,
            @NotNull String resultName,
            DBNResultSet resultSet,
            int updateCount) throws SQLException {
        super(executionProcessor, resultName, updateCount);

        ConnectionHandler connection = nd(executionProcessor.getConnection());
        int fetchBlockSize = executionProcessor.getExecutionInput().getResultSetFetchBlockSize();
        dataModel = new ResultSetDataModel<>(resultSet, connection, fetchBlockSize);
    }

    private StatementExecutionSettings getQueryExecutionSettings() {
        ExecutionEngineSettings settings = ExecutionEngineSettings.getInstance(getProject());
        return settings.getStatementExecutionSettings();
    }

    public StatementExecutionCursorResult(
            StatementExecutionProcessor executionProcessor,
            @NotNull String resultName,
            int updateCount) throws SQLException {
        super(executionProcessor, resultName, updateCount);
    }

    @Override
    @NotNull
    public StatementExecutionCursorProcessor getExecutionProcessor() {
        return (StatementExecutionCursorProcessor) super.getExecutionProcessor();
    }

    public void reload() {
        StatementExecutionCursorProcessor executionProcessor = getExecutionProcessor();
        ConnectionAction.invoke("Reload data", false, executionProcessor, action -> {
            Progress.background(getProject(), action, false,
                    "Loading data",
                    "Reloading result for " + executionProcessor.getStatementName(),
                    progress -> {
                        StatementExecutionResultForm resultForm = getForm();
                        if (Checks.isValid(resultForm)) {
                            StatementExecutionContext context = executionProcessor.initExecutionContext();
                            context.set(ExecutionStatus.EXECUTING, true);

                            try {
                                resultForm.highlightLoading(true);
                                StatementExecutionInput executionInput = getExecutionInput();

                                String statementText = executionInput.getExecutableStatementText();
                                statementText = DatasetFilterUtil.appendLimitIfLack(statementText, 0, 100, getProject(),
                                        Objects.requireNonNull(executionInput.getConnection()).getDatabaseType());

                                try {
                                    ConnectionHandler connection = getConnection();
                                    SchemaId currentSchema = getDatabaseSchema();
                                    DBNConnection conn = connection.getMainConnection(currentSchema);
                                    DBNStatement<?> statement = conn.createStatement();
                                    statement.setQueryTimeout(executionInput.getExecutionTimeout());
                                    statement.setFetchSize(executionInput.getResultSetFetchBlockSize());
                                    statement.execute(statementText);
                                    DBNResultSet resultSet = statement.getResultSet();
                                    if (resultSet != null) {
                                        loadResultSet(resultSet);
                                    }
                                } catch (final SQLException e) {
                                    conditionallyLog(e);
                                    Messages.showErrorDialog(getProject(), "Could not perform reload operation.", e);
                                }

                                NotifyUtil.INSTANCE.notifyInfo(getProject(), statementText);
                            } finally {
                                calculateExecDuration();
                                resultForm.highlightLoading(false);
                                context.reset();
                            }
                        }
                    });
        });
    }

    public void loadResultSet(DBNResultSet resultSet) throws SQLException {
        StatementExecutionResultForm resultForm = getForm();
        if (Checks.isValid(resultForm)) {
            int rowCount = Math.max(dataModel == null ? 0 : dataModel.getRowCount() + 1, 100);
            dataModel = new ResultSetDataModel<>(resultSet, getConnection(), rowCount);
            resultForm.rebuildForm();
            resultForm.updateVisibleComponents();
        }
    }

    @Nullable
    @Override
    public StatementExecutionResultForm createForm() {
        datasetEditor = loadDatasetEditorIfSingleTableQuery();

        StatementExecutionResultForm form = new StatementExecutionResultForm(this, datasetEditor);
        form.updateVisibleComponents();
        return form;
    }

    private @Nullable DatasetEditor loadDatasetEditorIfSingleTableQuery() {
        ExecutablePsiElement executablePsiElement = getExecutablePsiElement();
        if (!(executablePsiElement instanceof MockExecutablePsiElement)) {
            return null;
        }

        MockExecutablePsiElement mockExecutablePsiElement = (MockExecutablePsiElement) executablePsiElement;
        String tableName = mockExecutablePsiElement.getIfSingleTableQuery();
        if (tableName == null) {
            return null;
        }

        Project project = executablePsiElement.getProject();
        FileEditor selectedEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        if (selectedEditor == null) {
            return null;
        }

        ConnectionHandler connection = getExecutionProcessor().getConnection();
        if (connection == null) {
            return null;
        }

        VirtualFile virtualFile = selectedEditor.getFile();
        FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
        SchemaId schema = contextManager.getDatabaseSchema(virtualFile);
        if (schema == null) {
            return null;
        }

        DBObjectBundle objectBundle = connection.getObjectBundle();
        DBSchema dbSchema = objectBundle.getSchema(schema.getName());
        if (dbSchema == null) {
            throw new IllegalStateException("正在加载数据库元数据信息,请稍后再试!");
        }

        List<DBTable> dbTables = dbSchema.getTables();
        if (dbTables.isEmpty()) {
            throw new IllegalStateException("正在加载表元数据信息,请稍后再试!");
        }

        dbTables = dbTables.stream()
                .filter(it -> tableName.equals(it.getName()))
                .collect(Collectors.toList());
        if (dbTables.isEmpty()) {
            return null;
        }

        DBTable dbTable = dbTables.get(0);
        DBEditableObjectVirtualFile databaseFile = DatabaseFileSystem.getInstance().findOrCreateDatabaseFile(dbTable);
        if (databaseFile == null) {
            return null;
        }

        String conditionStr = mockExecutablePsiElement.getCondition();
        if (conditionStr != null) {
            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(project);
            DatasetFilterGroup filterGroup = filterManager.getFilterGroup(connection.getConnectionId(), tableName);
            String name = "AutoGenerate";
            Optional<DatasetFilter> optional = filterGroup.getFilters().stream()
                    .filter(it -> it.getName().equals(name))
                    .findFirst();

            DatasetCustomFilter customFilter;
            if (optional.isPresent()) {
                customFilter = (DatasetCustomFilter) optional.get();
                customFilter.setCondition(conditionStr);
            } else {
                customFilter = filterGroup.createCustomFilter(false, name);
                customFilter.setCondition(conditionStr);
            }


            filterManager.setActiveFilter(dbTable, customFilter);
        }

        DBDatasetVirtualFile datasetFile = nn(databaseFile.getContentFile(DBContentType.DATA));
        DBDataset dataset = datasetFile.getObject();

        DatasetEditor datasetEditor = new DatasetEditor(databaseFile, dataset);
        datasetEditor.loadData(DatasetEditorManager.INITIAL_LOAD_INSTRUCTIONS);

        return datasetEditor;
    }

    public void fetchNextRecords() {
        Project project = getProject();
        Progress.background(project, getConnection(), false,
                "Loading data",
                "Loading next records for " + getExecutionProcessor().getStatementName(),
                progress -> {
                    StatementExecutionResultForm resultForm = getForm();
                    if (Checks.isValid(resultForm)) {
                        resultForm.highlightLoading(true);
                        try {
                            if (hasResult() && !dataModel.isResultSetExhausted()) {
                                int fetchBlockSize = getExecutionInput().getResultSetFetchBlockSize();
                                dataModel.fetchNextRecords(fetchBlockSize, false);
                                //tResult.accommodateColumnsSize();
                                if (dataModel.isResultSetExhausted()) {
                                    dataModel.closeResultSet();
                                }
                                resultForm.updateVisibleComponents();
                            }

                        } catch (SQLException e) {
                            conditionallyLog(e);
                            Messages.showErrorDialog(project, "Could not perform operation.", e);
                        } finally {
                            resultForm.highlightLoading(false);
                        }
                    }
                });
    }

    public ResultSetDataModel<?, ?> getTableModel() {
        return dataModel;
    }

    @Nullable
    public ResultSetTable<?> getResultTable() {
        StatementExecutionResultForm resultForm = getForm();
        return Checks.isValid(resultForm) ? resultForm.getResultTable() : null;
    }

    public boolean hasResult() {
        return dataModel != null;
    }

    public void navigateToResult() {
        StatementExecutionResultForm resultForm = getForm();
        if (Checks.isValid(resultForm)) {
            resultForm.show();
        }
    }

    @Override
    public void disposeInner() {
        dataModel = null;

        if (datasetEditor != null) {
            Disposer.dispose(datasetEditor);
        }

        super.disposeInner();
    }


    /********************************************************
     *                    Data Provider                     *
     ********************************************************/
    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        if (DataKeys.STATEMENT_EXECUTION_CURSOR_RESULT.is(dataId)) return this;
        return null;
    }
}
