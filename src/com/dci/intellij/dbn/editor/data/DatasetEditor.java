package com.dci.intellij.dbn.editor.data;

import javax.swing.JComponent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.common.LoggerFactory;
import com.dci.intellij.dbn.common.action.DBNDataKeys;
import com.dci.intellij.dbn.common.dispose.AlreadyDisposedException;
import com.dci.intellij.dbn.common.dispose.Disposable;
import com.dci.intellij.dbn.common.dispose.FailsafeUtil;
import com.dci.intellij.dbn.common.event.EventManager;
import com.dci.intellij.dbn.common.thread.SimpleBackgroundTask;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.thread.SimpleTask;
import com.dci.intellij.dbn.common.util.DataProviderSupplier;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.connection.ConnectionAction;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerRef;
import com.dci.intellij.dbn.connection.ConnectionProvider;
import com.dci.intellij.dbn.connection.ConnectionStatusListener;
import com.dci.intellij.dbn.connection.mapping.FileConnectionMappingProvider;
import com.dci.intellij.dbn.connection.transaction.TransactionAction;
import com.dci.intellij.dbn.connection.transaction.TransactionListener;
import com.dci.intellij.dbn.data.grid.options.DataGridSettingsChangeListener;
import com.dci.intellij.dbn.database.DatabaseMessageParserInterface;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterType;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModel;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelRow;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.editor.data.record.ui.DatasetRecordEditorDialog;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorState;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnSetup;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnState;
import com.dci.intellij.dbn.editor.data.structure.DatasetEditorStructureViewModel;
import com.dci.intellij.dbn.editor.data.ui.DatasetEditorForm;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.vfs.DBEditableObjectVirtualFile;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;

public class DatasetEditor extends UserDataHolderBase implements FileEditor, FileConnectionMappingProvider, Disposable, ConnectionProvider, DataProviderSupplier {
    private static final Logger LOGGER = LoggerFactory.createLogger();

    public static final DatasetLoadInstructions COL_VISIBILITY_STATUS_CHANGE_LOAD_INSTRUCTIONS = new DatasetLoadInstructions(true, true, true, true);
    public static final DatasetLoadInstructions CON_STATUS_CHANGE_LOAD_INSTRUCTIONS = new DatasetLoadInstructions(true, false, false, false);
    private DBObjectRef<DBDataset> datasetRef;
    private DBEditableObjectVirtualFile databaseFile;
    private DatasetEditorForm editorForm;
    private StructureViewModel structureViewModel;
    private ConnectionHandlerRef connectionHandlerRef;
    private DataEditorSettings settings;
    private Project project;
    private boolean isLoading;

    private String dataLoadError;
    private DatasetEditorState editorState = new DatasetEditorState();

    public DatasetEditor(DBEditableObjectVirtualFile databaseFile, DBDataset dataset) {
        this.project = dataset.getProject();
        this.databaseFile = databaseFile;
        this.datasetRef = DBObjectRef.from(dataset);
        this.settings = DataEditorSettings.getInstance(project);

        connectionHandlerRef = ConnectionHandlerRef.from(dataset.getConnectionHandler());
        editorForm = new DatasetEditorForm(this);



/*
        if (!EditorUtil.hasEditingHistory(databaseFile, project)) {
            load(true, true, false);
        }
*/
        Disposer.register(this, editorForm);

        EventManager.subscribe(project, TransactionListener.TOPIC, transactionListener);
        EventManager.subscribe(project, ConnectionStatusListener.TOPIC, connectionStatusListener);
        EventManager.subscribe(project, DataGridSettingsChangeListener.TOPIC, dataGridSettingsChangeListener);
    }

    @NotNull
    public DBDataset getDataset() {
        return FailsafeUtil.get(datasetRef.get(project));
    }

    public DataEditorSettings getSettings() {
        return settings;
    }

    @NotNull
    public DatasetEditorTable getEditorTable() {
        return getEditorForm().getEditorTable();
    }

    @NotNull
    public DatasetEditorForm getEditorForm() {
        return FailsafeUtil.get(editorForm);
    }

    public void showSearchHeader() {
        getEditorForm().showSearchHeader();
    }

    @NotNull
    public DatasetEditorModel getTableModel() {
        return getEditorTable().getModel();
    }



    public DBEditableObjectVirtualFile getDatabaseFile() {
        return databaseFile;
    }

    @NotNull
    public ConnectionHandler getActiveConnection() {
        return getConnectionHandler();
    }

    @Nullable
    public DBSchema getCurrentSchema() {
        return getDataset().getSchema();
    }

    public Project getProject() {
        return project;
    }

    @NotNull
    public JComponent getComponent() {
        return getEditorForm().getComponent();
    }

    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return getEditorTable();
    }

    @NonNls
    @NotNull
    public String getName() {
        return "Data";
    }

    @NotNull
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return editorState.clone();
    }

    public void setState(@NotNull FileEditorState fileEditorState) {
        if (fileEditorState instanceof DatasetEditorState) {
            editorState = (DatasetEditorState) fileEditorState;
        }
    }

    public DatasetEditorState getEditorState() {
        return editorState;
    }

    public boolean isModified() {
        return getTableModel().isModified();
    }

    public boolean isValid() {
        return true;
    }

    public void selectNotify() {

    }

    public void deselectNotify() {

    }

    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Nullable
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Nullable
    public StructureViewBuilder getStructureViewBuilder() {
        return new TreeBasedStructureViewBuilder() {
            @NotNull
            @Override
            public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
                return createStructureViewModel();
            }

            @NotNull
            public StructureViewModel createStructureViewModel() {
                // Structure does not change. so it can be cached.
                if (structureViewModel == null) {
                    structureViewModel = new DatasetEditorStructureViewModel(DatasetEditor.this);
                }
                return structureViewModel;
            }
        };
    }

    public static DatasetEditor getSelected(Project project) {
        if (project != null) {
            FileEditor[] fileEditors = FileEditorManager.getInstance(project).getSelectedEditors();
            for (FileEditor fileEditor : fileEditors) {
                if (fileEditor instanceof DatasetEditor) {
                    return (DatasetEditor) fileEditor;
                }
            }
        }
        return null;
    }

    /*******************************************************
     *                   Model operations                  *
     *******************************************************/
    public void fetchNextRecords(int records) {
        try {
            DatasetEditorModel model = getTableModel();
            model.fetchNextRecords(records, false);
            dataLoadError = null;
        } catch (SQLException e) {
            dataLoadError = e.getMessage();
/*
            String message = "Error loading data for " + getDataset().getQualifiedNameWithType() + ".\nCause: " + e.getMessage();
            MessageUtil.showErrorDialog(message, e);
*/
        } finally {
            EventManager.notify(project, DatasetLoadListener.TOPIC).datasetLoaded(databaseFile);
        }
    }

    public void loadData(final DatasetLoadInstructions instructions) {
        if (!isLoading) {
            new ConnectionAction("loading table data", this) {
                @Override
                protected void execute() {
                    setLoading(true);
                    new SimpleBackgroundTask("load table data") {
                        @Override
                        protected void execute() {
                            try {
                                if (!isDisposed()) {
                                    editorForm.showLoadingHint();
                                    editorForm.getEditorTable().cancelEditing();
                                    DatasetEditorTable oldEditorTable = instructions.isRebuild() ? editorForm.beforeRebuild() : null;
                                    try {
                                        DatasetEditorModel tableModel = getTableModel();
                                        tableModel.load(instructions.isUseCurrentFilter(), instructions.isKeepChanges());
                                        DatasetEditorTable editorTable = getEditorTable();
                                        editorTable.clearSelection();
                                    } finally {
                                        if (!isDisposed()) {
                                            editorForm.afterRebuild(oldEditorTable);
                                        }
                                    }
                                }
                                dataLoadError = null;
                            } catch (SQLException e) {
                                dataLoadError = e.getMessage();
                                handleLoadError(e, instructions);
                            } catch (Exception e) {
                                if (e != AlreadyDisposedException.INSTANCE) {
                                    LOGGER.error("Error loading table data", e);
                                }
                            } finally {
                                if (editorForm != null) {
                                    editorForm.hideLoadingHint();
                                }
                                setLoading(false);
                                EventManager.notify(getProject(), DatasetLoadListener.TOPIC).datasetLoaded(databaseFile);
                            }
                        }

                    }.start();
                }
            }.start();
        }

    }

    private void handleLoadError(final SQLException e, final DatasetLoadInstructions instr) {
        new SimpleLaterInvocator() {
            @Override
            protected void execute() {
                final DBDataset dataset = getDataset();
                if (!isDisposed()) {
                    focusEditor();
                    ConnectionHandler connectionHandler = getConnectionHandler();
                    DatabaseMessageParserInterface messageParserInterface = connectionHandler.getInterfaceProvider().getMessageParserInterface();
                    final DatasetFilterManager filterManager = DatasetFilterManager.getInstance(getProject());

                    final DatasetFilter filter = filterManager.getActiveFilter(dataset);
                    String datasetName = dataset.getQualifiedNameWithType();
                    if (connectionHandler.isValid()) {
                        if (filter == null || filter == DatasetFilterManager.EMPTY_FILTER || filter.getError() != null) {
                            if (instr.isDeliberateAction()) {
                                String message =
                                        "Error loading data for " + datasetName + ".\n" + (
                                                messageParserInterface.isTimeoutException(e) ?
                                                        "The operation was timed out. Please check your timeout configuration in Data Editor settings." :
                                                        "Database error message: " + e.getMessage());

                                MessageUtil.showErrorDialog(project, message);
                            }
                        } else {
                            String message =
                                    "Error loading data for " + datasetName + ".\n" + (
                                            messageParserInterface.isTimeoutException(e) ?
                                                    "The operation was timed out. Please check your timeout configuration in Data Editor settings." :
                                                    "Filter \"" + filter.getName() + "\" may be invalid.\n" +
                                                            "Database error message: " + e.getMessage());
                            String[] options = {"Edit filter", "Remove filter", "Ignore filter", "Cancel"};

                            MessageUtil.showErrorDialog(project, "Error", message, options, 0, new SimpleTask() {
                                @Override
                                protected void execute() {
                                    int option = getOption();
                                    DatasetLoadInstructions instructions = instr.clone();
                                    instructions.setDeliberateAction(true);

                                    if (option == 0) {
                                        filterManager.openFiltersDialog(dataset, false, false, DatasetFilterType.NONE);
                                        instructions.setUseCurrentFilter(true);
                                        loadData(instructions);
                                    } else if (option == 1) {
                                        filterManager.setActiveFilter(dataset, null);
                                        instructions.setUseCurrentFilter(true);
                                        loadData(instructions);
                                    } else if (option == 2) {
                                        filter.setError(e.getMessage());
                                        instructions.setUseCurrentFilter(false);
                                        loadData(instructions);
                                    }
                                }
                            });
                        }
                    } else {
                        String message =
                                "Error loading data for " + datasetName + ". Could not connect to database.\n" +
                                        "Database error message: " + e.getMessage();
                        MessageUtil.showErrorDialog(project, message);
                    }
                }

            }
        }.start();
    }


    private void focusEditor() {
        FileEditorManager.getInstance(project).openFile(databaseFile, true);
    }

    protected void setLoading(boolean loading) {
        if (this.isLoading != loading) {
            this.isLoading = loading;
            DatasetEditorTable editorTable = getEditorTable();
            editorTable.setLoading(loading);
            editorTable.revalidate();
            editorTable.repaint();
        }

    }

    public void deleteRecords() {
        DatasetEditorTable editorTable = getEditorTable();
        DatasetEditorModel model = getTableModel();

        int[] indexes = editorTable.getSelectedRows();
        model.deleteRecords(indexes);
    }

    public void insertRecord() {
        DatasetEditorTable editorTable = getEditorTable();
        DatasetEditorModel model = getTableModel();

        int[] indexes = editorTable.getSelectedRows();
        int rowIndex = indexes.length > 0 && indexes[0] < model.getRowCount() ? indexes[0] : 0;
        model.insertRecord(rowIndex);
    }

    public void duplicateRecord() {
        DatasetEditorTable editorTable = getEditorTable();
        DatasetEditorModel model = getTableModel();
        int[] indexes = editorTable.getSelectedRows();
        if (indexes.length == 1) {
            model.duplicateRecord(indexes[0]);
        }
    }

    public void openRecordEditor() {
        DatasetEditorTable editorTable = getEditorTable();
        DatasetEditorModel model = getTableModel();

        int index = editorTable.getSelectedRow();
        if (index == -1) index = 0;
        DatasetEditorModelRow row = model.getRowAtIndex(index);
        editorTable.stopCellEditing();
        editorTable.selectRow(row.getIndex());
        DatasetRecordEditorDialog editorDialog = new DatasetRecordEditorDialog(getProject(), row);
        editorDialog.show();
    }

    public void openRecordEditor(int index) {
        if (index > -1) {
            DatasetEditorModel model = getTableModel();

            DatasetEditorModelRow row = model.getRowAtIndex(index);
            DatasetRecordEditorDialog editorDialog = new DatasetRecordEditorDialog(getProject(), row);
            editorDialog.show();
        }
    }

    public boolean isInserting() {
        return getTableModel().isInserting();
    }

    public boolean isLoading() {
        return isLoading;
    }

    /**
     * The dataset is readonly. This can not be changed by the flag isReadonly
     */
    public boolean isReadonlyData() {
        return getTableModel().isReadonly();
    }

    public boolean isReadonly() {
        return editorState.isReadonly();
    }

    public DatasetColumnSetup getColumnSetup() {
        return editorState.getColumnSetup();
    }

    public void setReadonly(boolean readonly) {
        editorState.setReadonly(readonly);
    }

    public boolean isEditable() {
        DatasetEditorModel tableModel = getTableModel();
        ConnectionHandler connectionHandler = tableModel.getConnectionHandler();
        return tableModel.isEditable() && connectionHandler.isConnected();
    }

    public int getRowCount() {
        return getEditorTable().getRowCount();
    }


    @NotNull
    public ConnectionHandler getConnectionHandler() {
        return connectionHandlerRef.get();
    }

    /*******************************************************
     *                      Listeners                      *
     *******************************************************/
    private ConnectionStatusListener connectionStatusListener = new ConnectionStatusListener() {
        @Override
        public void statusChanged(String connectionId) {
            DatasetEditorTable editorTable = getEditorTable();
            ConnectionHandler connectionHandler = getConnectionHandler();
            if (connectionHandler.getId().equals(connectionId)) {
                editorTable.updateBackground(!connectionHandler.isConnected());
                if (connectionHandler.isConnected()) {
                    loadData(CON_STATUS_CHANGE_LOAD_INSTRUCTIONS);
                } else {
                    editorTable.cancelEditing();
                    editorTable.revalidate();
                    editorTable.repaint();
                }
            }
        }
    };

    private TransactionListener transactionListener = new TransactionListener() {
        public void beforeAction(ConnectionHandler connectionHandler, TransactionAction action) {
            if (connectionHandler == getConnectionHandler()) {
                DatasetEditorModel model = getTableModel();
                DatasetEditorTable editorTable = getEditorTable();
                if (action == TransactionAction.COMMIT) {

                    if (editorTable.isEditing()) {
                        editorTable.stopCellEditing();
                    }

                    if (isInserting()) {
                        try {
                            model.postInsertRecord(true, false, true);
                        } catch (SQLException e1) {
                            MessageUtil.showErrorDialog(project, "Could not create row in " + getDataset().getQualifiedNameWithType() + '.', e1);
                            model.cancelInsert(true);
                        }
                    }
                }

                if (action == TransactionAction.ROLLBACK || action == TransactionAction.ROLLBACK_IDLE) {
                    if (editorTable.isEditing()) {
                        editorTable.stopCellEditing();
                    }
                    if (isInserting()) {
                        model.cancelInsert(true);
                    }
                }
            }
        }

        public void afterAction(ConnectionHandler connectionHandler, TransactionAction action, boolean succeeded) {
            if (connectionHandler == getConnectionHandler()) {
                DatasetEditorModel model = getTableModel();
                DatasetEditorTable editorTable = getEditorTable();
                if (action == TransactionAction.COMMIT || action == TransactionAction.ROLLBACK) {
                    if (succeeded && isModified()) loadData(CON_STATUS_CHANGE_LOAD_INSTRUCTIONS);
                }

                if (action == TransactionAction.DISCONNECT) {
                    editorTable.stopCellEditing();
                    model.revertChanges();
                    editorTable.revalidate();
                    editorTable.repaint();
                }
            }
        }
    };

    private DataGridSettingsChangeListener dataGridSettingsChangeListener = new DataGridSettingsChangeListener() {
        @Override
        public void trackingColumnsVisibilityChanged(boolean visible) {
            loadData(COL_VISIBILITY_STATUS_CHANGE_LOAD_INSTRUCTIONS);
        }
    };



    /*******************************************************
     *                   Data Provider                     *
     *******************************************************/
    public DataProvider dataProvider = new DataProvider() {
        @Override
        public Object getData(@NonNls String dataId) {
            if (DBNDataKeys.DATASET_EDITOR.is(dataId)) {
                return DatasetEditor.this;
            }
            return null;
        }
    };

    @Nullable
    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public String getDataLoadError() {
        return dataLoadError;
    }

    /********************************************************
     *                    Disposable                        *
     ********************************************************/
    private boolean disposed;

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    public void dispose() {
        if (!disposed) {
            disposed = true;
            EventManager.unsubscribe(connectionStatusListener, transactionListener, dataGridSettingsChangeListener);
            editorForm = null;
            databaseFile = null;
            structureViewModel = null;
            settings = null;
        }
    }


    public List<DatasetColumnState> initColumnStates() {
        DatasetColumnSetup columnSetup = editorState.getColumnSetup();
        List<DatasetColumnState> columnStates = columnSetup.getColumnStates();
        DBDataset dataset = getDataset();
        if (columnStates.size() != dataset.getColumns().size()) {
            columnSetup.init(dataset);
        }
        return columnStates;
    }
}
