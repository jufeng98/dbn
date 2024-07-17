package com.dbn.object.common.list.action;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.cache.MetadataCacheService;
import com.dbn.common.action.ProjectAction;
import com.dbn.common.icon.Icons;
import com.dbn.common.thread.Progress;
import com.dbn.connection.ConnectionAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class ReloadObjectsAction extends ProjectAction {

    private final DBObjectList objectList;

    ReloadObjectsAction(DBObjectList objectList) {
        this.objectList = objectList;
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        presentation.setText(objectList.isLoaded() ? "Reload" : "Load");
        presentation.setIcon(Icons.ACTION_REFRESH);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        String listName = objectList.getName();

        ConnectionAction.invoke(
                objectList.isLoaded() ? "reloading the " + listName : "loading the " + listName, true, objectList,
                action -> Progress.prompt(project, objectList, true,
                        "Loading objects",
                        "Reloading " + objectList.getContentDescription(),
                        progress -> {
                            ConnectionHandler connectionHandler = objectList.getConnection();

                            MetadataCacheService cacheService = MetadataCacheService.getService(project);
                            try {
                                String schemaName = null;
                                BrowserTreeNode parent = objectList.getParent();
                                if (parent != null) {
                                    schemaName = parent.getSchemaName();
                                }
                                cacheService.clearCache(schemaName, project, connectionHandler.getMainConnection());
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                            connectionHandler.getMetaDataCache().reset();
                            objectList.reload();
                        }));
    }
}
