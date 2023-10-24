package com.dci.intellij.dbn.editor.data.filter;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.util.Dialogs;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.data.model.ColumnInfo;
import com.dci.intellij.dbn.editor.data.DatasetEditorManager;
import com.dci.intellij.dbn.editor.data.filter.ui.DatasetFilterDialog;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import lombok.val;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dci.intellij.dbn.common.component.Components.projectService;
import static com.dci.intellij.dbn.common.options.setting.Settings.newElement;

@State(
    name = DatasetFilterManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DatasetFilterManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DatasetFilterManager";

    public static final DatasetFilter EMPTY_FILTER = new DatasetEmptyFilter();
    private final Map<ConnectionId, Map<String, DatasetFilterGroup>> filters = new ConcurrentHashMap<>();

    private DatasetFilterManager(Project project) {
        super(project, COMPONENT_NAME);
    }

    public void switchActiveFilter(DBDataset dataset, DatasetFilter filter){
        Project project = dataset.getProject();
        DatasetFilterManager filterManager = DatasetFilterManager.getInstance(project);
        DatasetFilter activeFilter = filterManager.getActiveFilter(dataset);
        if (activeFilter != filter) {
            filterManager.setActiveFilter(dataset, filter);

        }
    }

    public int openFiltersDialog(DBDataset dataset, boolean isAutomaticPrompt, boolean createNewFilter, DatasetFilterType defaultFilterType) {
        AtomicInteger response = new AtomicInteger();
        Dialogs.show(() -> new DatasetFilterDialog(dataset, isAutomaticPrompt, createNewFilter, defaultFilterType), (dialog, exitCode) -> response.set(exitCode));
        return response.get();
    }

    public void createBasicFilter(@NotNull DBDataset dataset, String columnName, Object columnValue, ConditionOperator operator, boolean interactive) {
        DatasetFilterGroup filterGroup = getFilterGroup(dataset);
        DatasetBasicFilter filter = filterGroup.createBasicFilter(columnName, columnValue, operator, interactive);

        if (interactive) {
            Dialogs.show(() -> new DatasetFilterDialog(dataset, filter));
        } else {
            filter.setPersisted(true);
            filter.setTemporary(true);
            setActiveFilter(dataset, filter);
            DatasetEditorManager.getInstance(getProject()).reloadEditorData(dataset);
        }
    }

    public void createBasicFilter(@NotNull DBDataset dataset, String columnName, Object columnValue, ConditionOperator operator) {
        DatasetFilterGroup filterGroup = getFilterGroup(dataset);
        DatasetBasicFilter filter = filterGroup.createBasicFilter(columnName, columnValue, operator);

        filter.setPersisted(true);
        filter.setTemporary(true);
        setActiveFilter(dataset, filter);
        DatasetEditorManager.getInstance(getProject()).reloadEditorData(dataset);
    }

    public void createBasicFilter(DatasetFilterInput filterInput) {
        DBDataset dataset = filterInput.getDataset();
        DatasetFilterGroup filterGroup = getFilterGroup(dataset);
        DatasetBasicFilter filter = null;

        for (DBColumn column : filterInput.getColumns()) {
            Object value = filterInput.getColumnValue(column);
            if (filter == null) {
                filter = filterGroup.createBasicFilter(column.getName(), value, ConditionOperator.EQUAL);
            } else {
                filter.addCondition(column.getName(), value, ConditionOperator.EQUAL);
            }
        }

        if (filter != null) {
            filter.setPersisted(true);
            filter.setTemporary(true);
            setActiveFilter(dataset, filter);
            DatasetEditorManager.getInstance(getProject()).reloadEditorData(dataset);
        }
    }

    public void addConditionToFilter(DatasetBasicFilter filter, DBDataset dataset, ColumnInfo columnInfo, Object value, boolean interactive) {
        DatasetFilterGroup filterGroup = getFilterGroup(dataset);
        DatasetBasicFilterCondition condition = interactive ?
                new DatasetBasicFilterCondition(filter, columnInfo.getName(), value, ConditionOperator.EQUAL, true) :
                new DatasetBasicFilterCondition(filter, columnInfo.getName(), value, null);

        filter.addCondition(condition);
        filter.generateName();
        filterGroup.setActiveFilter(filter);
        if (interactive) {
            Dialogs.show(() -> new DatasetFilterDialog(dataset, false, false, DatasetFilterType.NONE));
        } else {
            DatasetEditorManager.getInstance(getProject()).reloadEditorData(dataset);
        }

    }



    public DatasetFilter getActiveFilter(@NotNull DBDataset dataset) {
        DatasetFilterGroup filterGroup = getFilterGroup(dataset);
        return filterGroup.getActiveFilter();
    }

    public void setActiveFilter(@NotNull DBDataset dataset, DatasetFilter filter) {
        DatasetFilterGroup filterGroup = getFilterGroup(dataset);
        filterGroup.setActiveFilter(filter);
    }

    private void addFilterGroup(@NotNull DatasetFilterGroup filterGroup) {
        ConnectionId connectionId = filterGroup.getConnectionId();
        String datasetName = filterGroup.getDatasetName();
        Map<String, DatasetFilterGroup> connectionFilters =
                filters.computeIfAbsent(connectionId, id -> new HashMap<>());

        connectionFilters.put(datasetName, filterGroup);
    }

    public DatasetFilterGroup getFilterGroup(@NotNull DBDataset dataset) {
        ConnectionHandler connection = Failsafe.nn(dataset.getConnection());
        ConnectionId connectionId = connection.getConnectionId();
        String datasetName = dataset.getQualifiedName();
        return getFilterGroup(connectionId, datasetName);
    }

    public DatasetFilterGroup getFilterGroup(@NotNull DatasetFilter filter) {
        ConnectionId connectionId = filter.getConnectionId();
        String datasetName = filter.getDatasetName();
        return getFilterGroup(connectionId, datasetName);
    }

    @NotNull
    public DatasetFilterGroup getFilterGroup(ConnectionId connectionId, String datasetName) {
        Map<String, DatasetFilterGroup> filterGroups = filters.computeIfAbsent(connectionId, id -> new ConcurrentHashMap<>());
        return filterGroups.computeIfAbsent(datasetName, n -> new DatasetFilterGroup(getProject(), connectionId, n));
    }

    public static DatasetFilterManager getInstance(@NotNull Project project) {
        return projectService(project, DatasetFilterManager.class);
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");

        for (val entry : filters.entrySet()) {
            ConnectionId connectionId = entry.getKey();
            ConnectionHandler connection = ConnectionHandler.get(connectionId);
            if (connection == null) continue;

            val filterLists = entry.getValue();
            for (val groupEntry : filterLists.entrySet()) {
                DatasetFilterGroup filterGroup = groupEntry.getValue();
                Element filterListElement = newElement(element, "filter-actions");
                filterGroup.writeConfiguration(filterListElement);
            }
        }
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        for (Element child : element.getChildren()) {
            DatasetFilterGroup filterGroup = new DatasetFilterGroup(getProject());
            filterGroup.readConfiguration(child);
            addFilterGroup(filterGroup);
        }
    }

}
