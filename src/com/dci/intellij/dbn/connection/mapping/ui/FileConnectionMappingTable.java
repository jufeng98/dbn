package com.dci.intellij.dbn.connection.mapping.ui;

import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.ui.Presentable;
import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.table.DBNColoredTableCellRenderer;
import com.dci.intellij.dbn.common.ui.table.DBNTable;
import com.dci.intellij.dbn.common.ui.table.DBNTableTransferHandler;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.common.util.Context;
import com.dci.intellij.dbn.common.util.Safe;
import com.dci.intellij.dbn.connection.ConnectionBundle;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContext;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.object.DBSchema;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

public class FileConnectionMappingTable extends DBNTable<FileConnectionMappingTableModel> {
    private final FileConnectionContextManager manager;

    public FileConnectionMappingTable(@NotNull DBNComponent parent, FileConnectionMappingTableModel model) {
        super(parent, model, true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultRenderer(FileConnectionContext.class, new CellRenderer());
        setTransferHandler(DBNTableTransferHandler.INSTANCE);
        initTableSorter();
        setCellSelectionEnabled(true);
        adjustRowHeight(2);
        getRowSorter().toggleSortOrder(2);
        accommodateColumnsSize();
        addMouseListener(new MouseListener());
        manager = FileConnectionContextManager.getInstance(getProject());

    }

    @Override
    protected int getMaxColumnWidth() {
        return 800;
    }

    @Override
    public void setModel(@NotNull TableModel dataModel) {
        super.setModel(dataModel);
        initTableSorter();
    }

    private static class CellRenderer extends DBNColoredTableCellRenderer {
        @Override
        protected void customizeCellRenderer(DBNTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
            FileConnectionContext entry = (FileConnectionContext) value;
            FileConnectionMappingTableModel model = (FileConnectionMappingTableModel) table.getModel();
            Object columnValue = model.getValue(entry, column);
            if (columnValue instanceof Presentable) {
                Presentable presentable = (Presentable) columnValue;
                //setIcon(presentable.getIcon());
            }

            if (columnValue instanceof ConnectionHandler ||
                    columnValue instanceof SchemaId ||
                    columnValue instanceof DatabaseSession) {
                //setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            } else if (columnValue instanceof VirtualFile) {
                VirtualFile virtualFile = (VirtualFile) columnValue;
                setIcon(virtualFile.getFileType().getIcon());
            }

            if (!selected) {
                ConnectionHandler connection = entry.getConnection();
                if (connection != null) {
                    JBColor color = connection.getEnvironmentType().getColor();
                    if (color != null) {
                        setBackground(Colors.softer(color, 30));
                        //setBackground(color.brighter());
                    }
                }
            }

            SimpleTextAttributes textAttributes = SimpleTextAttributes.REGULAR_ATTRIBUTES;
            String presentableValue = model.getPresentableValue(entry, column);
            append(presentableValue, textAttributes);
            setBorder(Borders.TEXT_FIELD_INSETS);
        }
    }

    public class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int clickCount = e.getClickCount();
            if (e.getButton() == MouseEvent.BUTTON1) {
                int selectedRow = getSelectedRow();
                int selectedColumn = getSelectedColumn();
                if (selectedRow > -1) {
                    FileConnectionContext mapping = (FileConnectionContext) getValueAt(selectedRow, 0);
                    if (mapping != null) {
                        VirtualFile file = mapping.getFile();
                        if (file != null) {
                            if (selectedColumn == 0 && clickCount == 2) {
                                FileEditorManager fileEditorManager = FileEditorManager.getInstance(getProject());
                                fileEditorManager.openFile(file, true);
                            } else if (selectedColumn == 1) {
                                promptConnectionSelector(mapping);
                            } else if (selectedColumn == 3) {
                                promptSchemaSelector(mapping);
                            } else if (selectedColumn == 4) {
                                promptSessionSelector(mapping);
                            }
                        }
                    }
                }
            }
        }
    }


    private void promptConnectionSelector(@NotNull FileConnectionContext mapping) {
        Project project = getProject();
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
        VirtualFile file = mapping.getFile();

        DefaultActionGroup actionGroup = new DefaultActionGroup();

        List<ConnectionHandler> connections = connectionBundle.getConnections();
        connections.stream().map(c -> new ConnectionAction(file, c)).forEach(a -> actionGroup.add(a));

        actionGroup.addSeparator();
        Collection<ConnectionHandler> virtualConnections = connectionBundle.getVirtualConnections();
        virtualConnections.stream().map(c -> new ConnectionAction(file, c)).forEach(a -> actionGroup.add(a));

        actionGroup.addSeparator();
        actionGroup.add(new ConnectionAction(file, null));
        promptSelector(actionGroup, a -> ((ConnectionAction) a).getConnectionId() == mapping.getConnectionId());
    }

    private void promptSchemaSelector(@NotNull FileConnectionContext mapping) {
        ConnectionHandler connection = mapping.getConnection();
        if (connection != null && !connection.isVirtual()) {
            Progress.modal(connection.getProject(), "Loading schemas", true, progress -> {
                List<DBSchema> schemas = connection.getObjectBundle().getSchemas();

                DefaultActionGroup actionGroup = new DefaultActionGroup();
                VirtualFile file = mapping.getFile();
                schemas.stream().map(schema -> new SchemaAction(file, schema.getIdentifier())).forEach(a -> actionGroup.add(a));
                promptSelector(actionGroup, a -> ((SchemaAction) a).getSchemaId() == mapping.getSchemaId());
            });
        }
    }

    private void promptSessionSelector(@NotNull FileConnectionContext mapping) {
        ConnectionHandler connection = mapping.getConnection();
        if (connection != null && !connection.isVirtual()) {
            DefaultActionGroup actionGroup = new DefaultActionGroup();
            VirtualFile file = mapping.getFile();

            List<DatabaseSession> sessions = connection.getSessionBundle().getSessions();
            sessions.stream().map(session -> new SessionAction(file, session)).forEach(a -> actionGroup.add(a));
            promptSelector(actionGroup, a -> ((SessionAction) a).getSession() == mapping.getSession());
        }
    }

    private void promptSelector(ActionGroup actionGroup, Condition<AnAction> preselectCondition) {
        Dispatch.runConditional(() -> {
            ListPopup popupBuilder = JBPopupFactory.getInstance().createActionGroupPopup(
                    null,
                    actionGroup,
                    Context.getDataContext(FileConnectionMappingTable.this),
                    JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                    true,
                    null,
                    30,
                    preselectCondition,
                    null);

            popupBuilder.showInScreenCoordinates(this, getPopupLocation());
        });
    }

    @NotNull
    private Point getPopupLocation() {
        Point location = getCellLocation(getSelectedRow(), getSelectedColumn());
        Rectangle rectangle = getCellRect(getSelectedRow(), getSelectedColumn(), true);
        location = new Point(
                (int) (location.getX() + rectangle.getWidth() / 2),
                (int) (location.getY() /*+ rectangle.getHeight()*/));
        return location;
    }



    private class ConnectionAction extends AnAction implements DumbAware {
        private final VirtualFile virtualFile;
        private final ConnectionRef connection;
        private ConnectionAction(VirtualFile virtualFile, ConnectionHandler connection) {
            super(
                Safe.call(connection, c -> c.getName(), "No Connection"), null,
                Safe.call(connection, c -> c.getIcon()));
            this.virtualFile = virtualFile;
            this.connection = ConnectionRef.of(connection);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            ConnectionHandler connection = getConnection();
            manager.setConnection(virtualFile, connection);
            notifyModelChanges(virtualFile);
        }

        @Nullable
        private ConnectionHandler getConnection() {
            return ConnectionRef.get(connection);
        }

        public ConnectionId getConnectionId() {
            ConnectionHandler connection = getConnection();
            return connection == null ? null : connection.getConnectionId();
        }
    }

    @Getter
    private class SchemaAction extends AnAction implements DumbAware {
        private final VirtualFile virtualFile;
        private final SchemaId schemaId;
        private SchemaAction(VirtualFile virtualFile, SchemaId schemaId) {
            super(Actions.adjustActionName(schemaId.getName()), "", schemaId.getIcon());
            this.virtualFile = virtualFile;
            this.schemaId = schemaId;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            manager.setDatabaseSchema(virtualFile, schemaId);
            notifyModelChanges(virtualFile);
        }
    }

    @Getter
    private class SessionAction extends AnAction implements DumbAware {
        private final VirtualFile virtualFile;
        private final DatabaseSession session;
        private SessionAction(VirtualFile virtualFile, DatabaseSession session) {
            super(Actions.adjustActionName(session.getName()), "", session.getIcon());
            this.virtualFile = virtualFile;
            this.session = session;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            manager.setDatabaseSession(virtualFile, session);
            notifyModelChanges(virtualFile);
        }
    }

    private void notifyModelChanges(VirtualFile virtualFile) {
        FileConnectionMappingTableModel model = getModel();
        int rowIndex = model.indexOf(virtualFile);
        if (rowIndex > -1) {
            model.notifyRowChange(rowIndex);
        }
    }
}
