package com.dbn.object.common.list.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.action.ProjectAction;
import com.dbn.common.thread.Progress;
import com.dbn.connection.ConnectionAction;
import com.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

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
                            objectList.getConnection().getMetaDataCache().reset();
                            objectList.reload();
                        }));
    }
}
