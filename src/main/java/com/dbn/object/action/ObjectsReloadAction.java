package com.dbn.object.action;

import com.dbn.cache.MetadataCacheService;
import com.dbn.common.action.BasicAction;
import com.dbn.common.thread.Background;
import com.dbn.connection.ConnectionAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ObjectsReloadAction extends BasicAction {

    private final DBObjectList<?> objectList;

    ObjectsReloadAction(DBObjectList<?> objectList) {
        super((objectList.isLoaded() ? "Reload " : "Load ") + objectList.getName());
        this.objectList = objectList;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String listName = objectList.getName();
        boolean loaded = objectList.isLoaded();

        String description = loaded ? "reloading the " + listName : "loading the " + listName;
        ConnectionAction.invoke(description, true, objectList, action -> reloadObjectList());
    }

    private void reloadObjectList() {
        Project project = objectList.getProject();
        Background.run(project, () -> {
            ConnectionHandler connectionHandler = objectList.getConnection();

            MetadataCacheService cacheService = MetadataCacheService.getService(project);
            cacheService.clearCache(objectList.getParentSchemaName(), project, connectionHandler.getMainConnection());

            connectionHandler.getMetaDataCache().reset();
            objectList.reload();
        });
    }
}