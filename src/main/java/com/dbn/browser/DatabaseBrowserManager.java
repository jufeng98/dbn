package com.dbn.browser;

import com.dbn.DatabaseNavigator;
import com.dbn.browser.model.BrowserTreeModel;
import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.browser.model.ConnectionBrowserTreeModel;
import com.dbn.browser.options.BrowserDisplayMode;
import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.browser.options.ObjectFilterChangeListener;
import com.dbn.browser.ui.BrowserToolWindowForm;
import com.dbn.browser.ui.DatabaseBrowserForm;
import com.dbn.browser.ui.DatabaseBrowserTree;
import com.dbn.common.component.PersistentState;
import com.dbn.common.component.ProjectComponentBase;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.filter.Filter;
import com.dbn.common.latent.Latent;
import com.dbn.common.listener.DBNFileEditorManagerListener;
import com.dbn.common.options.setting.BooleanSetting;
import com.dbn.common.thread.Background;
import com.dbn.common.thread.Dispatch;
import com.dbn.connection.ConnectionBundle;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.ConnectionManager;
import com.dbn.connection.DatabaseType;
import com.dbn.connection.SchemaId;
import com.dbn.connection.config.ConnectionBundleSettings;
import com.dbn.connection.config.ConnectionDatabaseSettings;
import com.dbn.connection.config.ConnectionDetailSettings;
import com.dbn.connection.config.ConnectionSettings;
import com.dbn.editor.data.options.DataEditorSettings;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.DBSchema;
import com.dbn.object.DBTable;
import com.dbn.object.DBView;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBObjectBundle;
import com.dbn.object.common.list.DBObjectList;
import com.dbn.object.common.list.DBObjectListContainer;
import com.dbn.object.type.DBObjectType;
import com.dbn.options.ProjectSettings;
import com.dbn.utils.TooltipUtils;
import com.dbn.vfs.DBVirtualFile;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import lombok.Getter;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dbn.browser.DatabaseBrowserUtils.isSkipBrowserAutoscroll;
import static com.dbn.common.component.Components.projectService;
import static com.dbn.common.dispose.Failsafe.nn;
import static com.dbn.common.options.setting.Settings.connectionIdAttribute;
import static com.dbn.common.options.setting.Settings.newElement;
import static com.dbn.common.options.setting.Settings.stringAttribute;
import static com.dbn.object.type.DBObjectType.COLUMN;
import static com.dbn.object.type.DBObjectType.TABLE;
import static com.dbn.object.type.DBObjectType.VIEW;

@Getter
@State(
        name = DatabaseBrowserManager.COMPONENT_NAME,
        storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DatabaseBrowserManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DatabaseBrowserManager";
    public static final String TOOL_WINDOW_ID = "DB Browser";

    private final BooleanSetting autoscrollFromEditor = new BooleanSetting("autoscroll-from-editor", true);
    private final BooleanSetting autoscrollToEditor = new BooleanSetting("autoscroll-to-editor", false);
    private final BooleanSetting showObjectProperties = new BooleanSetting("show-object-properties", true);

    private final transient Latent<BrowserToolWindowForm> toolWindowForm = Latent.basic(this::createToolWindowForm);
    private final Key<String> dbNameKey = Key.create("dbn.connection.url.dbName");

    private BrowserToolWindowForm createToolWindowForm() {
        return Dispatch.call(true, () -> new BrowserToolWindowForm(this, getProject()));
    }

    private DatabaseBrowserManager(Project project) {
        super(project, COMPONENT_NAME);

        ProjectEvents.subscribe(project, this, FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener());
        ProjectEvents.subscribe(project, this, ObjectFilterChangeListener.TOPIC, objectFilterChangeListener());
    }

    public static DatabaseBrowserManager getInstance(@NotNull Project project) {
        return projectService(project, DatabaseBrowserManager.class);
    }

    @Nullable
    public DatabaseBrowserTree getActiveBrowserTree() {
        BrowserToolWindowForm toolWindowForm = this.toolWindowForm.value();
        return toolWindowForm == null ? null : toolWindowForm.getActiveBrowserTree();
    }

    @Nullable
    public ConnectionHandler getActiveConnection() {
        DatabaseBrowserTree activeBrowserTree = getActiveBrowserTree();
        if (activeBrowserTree == null) return null;

        BrowserTreeModel model = activeBrowserTree.getModel();
        if (model instanceof ConnectionBrowserTreeModel treeModel) {
            return treeModel.getConnection();
        }

        BrowserTreeNode node = activeBrowserTree.getSelectedNode();
        if (node != null && !(node instanceof ConnectionBundle)) {
            return node.getConnection();
        }

        return null;
    }

    @NotNull
    @SuppressWarnings("DataFlowIssue")
    public ToolWindow getBrowserToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(getProject());
        return toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
    }

    @NotNull
    public BrowserToolWindowForm getToolWindowForm() {
        return nn(toolWindowForm.get());
    }

    public void navigateToElement(@Nullable BrowserTreeNode treeNode, boolean focus, boolean scroll) {
        Dispatch.run(() -> {
            ToolWindow toolWindow = getBrowserToolWindow();

            toolWindow.show(null);
            if (treeNode == null) return;

            DatabaseBrowserForm browserForm = getBrowserForm();
            browserForm.selectElement(treeNode, focus, scroll);
        });
    }

    public DatabaseBrowserForm getBrowserForm() {
        return getToolWindowForm().getBrowserForm();
    }

    public void selectConnection(ConnectionId connectionId) {
        getBrowserForm().selectConnection(connectionId);
    }

    @Nullable
    public ConnectionId getSelectedConnectionId() {
        return getBrowserForm().getSelectedConnection();
    }

    @Nullable
    public ConnectionHandler getSelectedConnection() {
        ConnectionId connectionId = getSelectedConnectionId();
        return ConnectionHandler.get(connectionId);
    }


    private void navigateToElement(@Nullable BrowserTreeNode treeNode, boolean scroll) {
        if (treeNode == null) return;

        Dispatch.run(() -> {
            DatabaseBrowserForm browserForm = getBrowserForm();
            browserForm.selectElement(treeNode, false, scroll);
        });
    }

    public boolean isVisible() {
        ToolWindow toolWindow = getBrowserToolWindow();
        return toolWindow.isVisible();
    }

    /***************************************
     *     FileEditorManagerListener       *
     ***************************************/
    public static void scrollToSelectedElement(ConnectionHandler connection) {
        Dispatch.run(() -> {
            Project project = connection.getProject();
            DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
            BrowserToolWindowForm toolWindowForm = browserManager.getToolWindowForm();
            ConnectionId connectionId = connection.getConnectionId();
            DatabaseBrowserTree browserTree = toolWindowForm.getBrowserTree(connectionId);
            if (browserTree != null && browserTree.getTargetSelection() != null) {
                browserTree.scrollToSelectedElement();
            }
        });
    }

    public boolean isSingleTreeMode() {
        DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(getProject());
        return browserSettings.getGeneralSettings().getDisplayMode() == BrowserDisplayMode.SIMPLE;
    }

    /**********************************************************
     *                       Listeners                        *
     **********************************************************/
    @NotNull
    private ObjectFilterChangeListener objectFilterChangeListener() {
        return new ObjectFilterChangeListener() {
            @Override
            public void typeFiltersChanged(ConnectionId connectionId) {
                if (toolWindowForm.loaded()) {
                    ConnectionHandler connection = ConnectionHandler.get(connectionId);
                    if (connection == null) {
                        getBrowserForm().rebuildTree();
                    } else {
                        connection.getObjectBundle().rebuildTreeChildren();
                    }
                }
            }

            @Override
            public void nameFiltersChanged(ConnectionId connectionId, @NotNull DBObjectType... objectTypes) {
                ConnectionHandler connection = ConnectionHandler.get(connectionId);
                if (toolWindowForm.loaded() && connection != null && objectTypes.length > 0) {
                    connection.getObjectBundle().refreshTreeChildren(objectTypes);
                }
            }
        };
    }


    @SuppressWarnings("unused")
    public Filter<BrowserTreeNode> getObjectTypeFilter() {
        DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(getProject());
        return browserSettings.getFilterSettings().getObjectTypeFilterSettings().getElementFilter();
    }

    private boolean isMetadataLoading(DBObject dbObject, DBObjectType dbObjectType) {
        DBObjectList<DBObject> dbObjectList = dbObject.getChildObjectList(dbObjectType);
        if (dbObjectList == null) {
            return false;
        }
        return dbObjectList.isLoadingInBackground();
    }

    @SuppressWarnings("unused")
    public void navigateToElement(Project project, Set<String> tableNames, String columnName) {
        ToolWindow toolWindow = getBrowserToolWindow();

        toolWindow.show(() -> {
            String dbName = getFirstConnectionConfigDbName(project);
            if (dbName == null) {
                TooltipUtils.INSTANCE.showTooltip("无法跳转,数据库url链接无效!", project);
                return;
            }

            DBObjectBundle objectBundle = switchToFirstConnectionAndGetObjectBundle(project);
            if (objectBundle == null) {
                TooltipUtils.INSTANCE.showTooltip("无法跳转,请先连接数据库!", project);
                return;
            }

            DBSchema dbSchema = objectBundle.getSchema(dbName);
            if (dbSchema == null) {
                TooltipUtils.INSTANCE.showTooltip("正在加载数据库元数据信息,请稍后再试!", project);
                return;
            }

            List<DBTable> dbTables = dbSchema.getTables();
            List<DBView> dbViews = dbSchema.getViews();
            if (CollectionUtils.isEmpty(dbTables)) {
                TooltipUtils.INSTANCE.showTooltip("正在加载表元数据信息,请稍后再试!", project);
                return;
            }

            boolean loading = isMetadataLoading(dbSchema, TABLE) || isMetadataLoading(dbSchema, VIEW);
            if (loading) {
                TooltipUtils.INSTANCE.showTooltip("表元数据信息尚未加载完成,请稍后再试!", project);
                return;
            }

            dbTables = dbTables.stream()
                    .filter(it -> tableNames.contains(it.getName()))
                    .collect(Collectors.toList());

            dbViews = dbViews.stream()
                    .filter(it -> tableNames.contains(it.getName()))
                    .toList();

            if (dbTables.isEmpty() && dbViews.isEmpty()) {
                TooltipUtils.INSTANCE.showTooltip("无法跳转,在" + dbSchema.getName() + "数据库中未找到表:" + tableNames, project);
                return;
            }

            if (columnName == null) {
                if (!dbTables.isEmpty()) {
                    dbTables.get(0).navigate(true);
                } else {
                    dbViews.get(0).navigate(true);
                }
                return;
            }

            List<DBColumn> dbColumns = dbTables.stream()
                    .map(DBDataset::getColumns)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            List<DBColumn> viewDbColumns = dbViews.stream()
                    .map(DBDataset::getColumns)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            if (dbColumns.isEmpty() && viewDbColumns.isEmpty()) {
                TooltipUtils.INSTANCE.showTooltip("正在加载列元数据信息,请稍后再试!", project);
                return;
            }

            loading = dbTables.stream().anyMatch(it -> isMetadataLoading(it, COLUMN))
                    || dbViews.stream().anyMatch(it -> isMetadataLoading(it, COLUMN));
            if (loading) {
                TooltipUtils.INSTANCE.showTooltip("列元数据信息尚未加载完成,请稍后再试!", project);
                return;
            }

            dbColumns = dbColumns.stream()
                    .filter(it -> columnName.equals(it.getName()))
                    .toList();

            viewDbColumns = viewDbColumns.stream()
                    .filter(it -> columnName.equals(it.getName()))
                    .toList();

            if (dbColumns.isEmpty() && viewDbColumns.isEmpty()) {
                TooltipUtils.INSTANCE.showTooltip("无法跳转,在" + tableNames + "中未能解析列" + columnName + "!", project);
                return;
            }

            if (dbColumns.size() == 1) {
                dbColumns.get(0).navigate(true);
                return;
            }

            if (viewDbColumns.size() == 1) {
                viewDbColumns.get(0).navigate(true);
                return;
            }

            TooltipUtils.INSTANCE.showTooltip("无法跳转,在" + tableNames + "中解析到同样名称的列" + columnName + "!", project);
        });

    }

    private DBObjectBundle switchToFirstConnectionAndGetObjectBundle(Project project) {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);

        ConnectionId connectionId = getFirstConnectionId(project);

        browserManager.selectConnection(connectionId);
        ConnectionHandler connection = browserManager.getSelectedConnection();
        if (!ConnectionHandler.isLiveConnection(connection)) {
            return null;
        }

        return connection.getObjectBundle();
    }

    public @Nullable ConnectionDatabaseSettings getFirstConnectionConfig(Project project) {
        ProjectSettings projectSettings = DataEditorSettings.getInstance(project).getParent();
        if (projectSettings == null) {
            return null;
        }

        ConnectionBundleSettings connectionSettingsList = projectSettings.getConnectionSettings();
        List<ConnectionSettings> connections = connectionSettingsList.getConnections();
        if (CollectionUtils.isEmpty(connections)) {
            return null;
        }

        ConnectionSettings connectionSettings = connections.get(0);
        if (connectionSettings == null) {
            return null;
        }

        return connectionSettings.getDatabaseSettings();
    }

    public @Nullable ConnectionId getFirstConnectionId(Project project) {
        ConnectionDatabaseSettings config = getFirstConnectionConfig(project);
        if (config == null) {
            return null;
        }

        return config.getConnectionId();
    }

    public @Nullable String getFirstConnectionConfigDbName(Project project) {
        ConnectionDatabaseSettings config = getFirstConnectionConfig(project);
        if (config == null) {
            return null;
        }

        val url = config.getConnectionUrl();
        var dbName = project.getUserData(dbNameKey);
        if (dbName != null) {
            return dbName;
        }

        dbName = resolveUrlDbName(url);

        project.putUserData(dbNameKey, dbName);

        return dbName;
    }

    public @Nullable DBSchema getFirstConnectionConfigDbScheme(Project project) {
        String dbName = getFirstConnectionConfigDbName(project);

        DBObjectBundle objectBundle = switchToFirstConnectionAndGetObjectBundle(project);
        if (objectBundle == null) {
            return null;
        }

        return objectBundle.getSchema(dbName);
    }

    public DatabaseType getFirstConnectionType(Project project) {
        ConnectionDatabaseSettings config = getFirstConnectionConfig(project);
        if (config == null) {
            return DatabaseType.GENERIC;
        }

        return config.getDatabaseType();
    }

    public String getFirstConnectionDsName(Project project) {
        ConnectionDatabaseSettings config = getFirstConnectionConfig(project);
        if (config == null) {
            return "";
        }

        return config.getName();
    }

    private @Nullable String resolveUrlDbName(String url) {
        try {
            val uri = URI.create(url.substring(5));
            return uri.getPath().substring(1);
        } catch (Exception e) {
            return null;
        }
    }

    @NotNull
    private FileEditorManagerListener fileEditorManagerListener() {
        return new DBNFileEditorManagerListener() {
            @Override
            public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                if (!autoscrollFromEditor.value()) return;
                if (isSkipBrowserAutoscroll(file)) return;

                if (file instanceof DBVirtualFile databaseVirtualFile) {
                    DBObject object = databaseVirtualFile.getObject();
                    if (object != null) {
                        navigateToElement(object, true);
                    } else {
                        ConnectionHandler connection = databaseVirtualFile.ensureConnection();
                        navigateToElement(connection.getObjectBundle(), false);
                    }
                }
            }

            @Override
            public void whenSelectionChanged(@NotNull FileEditorManagerEvent event) {
                if (!autoscrollFromEditor.value()) return;

                VirtualFile oldFile = event.getOldFile();
                VirtualFile newFile = event.getNewFile();

                if (newFile == null) return;
                if (isSkipBrowserAutoscroll(newFile)) return;
                if (Objects.equals(oldFile, newFile)) return;

                if (newFile instanceof DBVirtualFile virtualFile) {
                    DBObject object = virtualFile.getObject();
                    if (object != null) {
                        navigateToElement(object, true);
                    } else {
                        ConnectionHandler connection = virtualFile.ensureConnection();
                        FileEditor oldEditor = event.getOldEditor();
                        SchemaId schemaId = virtualFile.getSchemaId();
                        boolean scroll = oldEditor != null && oldEditor.isValid();

                        Background.run(getProject(), () -> {
                            BrowserTreeNode treeNode = schemaId == null ?
                                    connection.getObjectBundle() :
                                    connection.getSchema(schemaId);

                            navigateToElement(treeNode, scroll);
                        });
                    }
                }
            }
        };
    }

    public void showObjectProperties(boolean visible) {
        BrowserToolWindowForm toolWindowForm = getToolWindowForm();
        if (visible) {
            toolWindowForm.showObjectProperties();
        } else {
            toolWindowForm.hideObjectProperties();
        }
        showObjectProperties.setValue(visible);
    }

    public List<DBObject> getSelectedObjects() {
        List<DBObject> selectedObjects = new ArrayList<>();
        DatabaseBrowserTree activeBrowserTree = getActiveBrowserTree();
        if (activeBrowserTree == null) return selectedObjects;

        TreePath[] selectionPaths = activeBrowserTree.getSelectionPaths();
        if (selectionPaths == null) return selectedObjects;

        for (TreePath treePath : selectionPaths) {
            Object lastPathComponent = treePath.getLastPathComponent();
            if (lastPathComponent instanceof DBObject object) {
                selectedObjects.add(object);
            }
        }
        return selectedObjects;
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        autoscrollToEditor.writeConfiguration(element);
        autoscrollFromEditor.writeConfiguration(element);
        showObjectProperties.writeConfiguration(element);
        storeTouchedNodes(element);
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        autoscrollToEditor.readConfiguration(element);
        autoscrollFromEditor.readConfiguration(element);
        showObjectProperties.readConfiguration(element);
        initTouchedNodes(element);
    }

    private void storeTouchedNodes(Element element) {
        Element nodesElement = newElement(element, "loaded-nodes");

        ConnectionManager connectionManager = ConnectionManager.getInstance(getProject());
        List<ConnectionHandler> connections = connectionManager.getConnections();
        for (ConnectionHandler connection : connections) {
            ConnectionDetailSettings settings = connection.getSettings().getDetailSettings();
            if (!settings.isRestoreWorkspaceDeep()) {
                continue;
            }

            Element connectionElement = new Element("connection");

            boolean addConnectionElement = false;
            DBObjectBundle objectBundle = connection.getObjectBundle();
            DBObjectList<?> schemas = objectBundle.getObjectList(DBObjectType.SCHEMA);
            if (schemas == null || !schemas.isLoaded()) {
                continue;
            }

            for (DBSchema schema : objectBundle.getSchemas()) {
                List<DBObjectType> objectTypes = new ArrayList<>();
                schema.visitChildObjects(o -> {
                    if (o.isLoaded() || o.isLoading()) {
                        objectTypes.add(o.getObjectType());
                    }
                }, true);

                if (!objectTypes.isEmpty()) {
                    Element schemaElement = newElement(connectionElement, "schema");
                    schemaElement.setAttribute("name", schema.getName());
                    schemaElement.setAttribute("object-types", DBObjectType.toCsv(objectTypes));
                    addConnectionElement = true;
                }
            }

            if (addConnectionElement) {
                connectionElement.setAttribute("connection-id", connection.getConnectionId().id());
                nodesElement.addContent(connectionElement);
            }
        }
    }

    private void initTouchedNodes(Element element) {
        Element nodesElement = element.getChild("loaded-nodes");
        if (nodesElement == null) {
            return;
        }

        Project project = getProject();
        List<Element> connectionElements = nodesElement.getChildren();

        for (Element connectionElement : connectionElements) {
            ConnectionId connectionId = connectionIdAttribute(connectionElement, "connection-id");
            ConnectionHandler connection = ConnectionHandler.get(connectionId);
            if (connection == null) {
                continue;
            }

            ConnectionDetailSettings settings = connection.getSettings().getDetailSettings();
            if (!settings.isRestoreWorkspaceDeep()) {
                continue;
            }

            DBObjectBundle objectBundle = connection.getObjectBundle();
            List<Element> schemaElements = connectionElement.getChildren();

            for (Element schemaElement : schemaElements) {
                String schemaName = stringAttribute(schemaElement, "name");
                DBSchema schema = objectBundle.getSchema(schemaName);
                if (schema == null) {
                    continue;
                }

                Background.run(project, () -> {
                    String objectTypesAttr = stringAttribute(schemaElement, "object-types");
                    List<DBObjectType> objectTypes = DBObjectType.fromCsv(objectTypesAttr);

                    for (DBObjectType objectType : objectTypes) {
                        DBObjectListContainer childObjects = schema.getChildObjects();
                        if (childObjects == null) {
                            continue;
                        }

                        childObjects.loadObjects(objectType);
                    }
                });
            }
        }
    }


    @Override
    public void disposeInner() {
        toolWindowForm.set(null);
        super.disposeInner();
    }

    public DatabaseBrowserSettings getSettings() {
        return DatabaseBrowserSettings.getInstance(getProject());
    }

    public void changeDisplayMode(BrowserDisplayMode mode) {
        DatabaseBrowserSettings browserSettings = getSettings();
        browserSettings.getGeneralSettings().setDisplayMode(mode);
        getToolWindowForm().rebuild();

    }
}
