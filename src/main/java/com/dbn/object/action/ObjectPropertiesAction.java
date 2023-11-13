package com.dbn.object.action;

import com.dbn.common.util.Messages;
import com.dbn.object.common.DBObject;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ObjectPropertiesAction<T extends DBObject> extends AnObjectAction<T> {
    public ObjectPropertiesAction(T object) {
        super("Properties", null, object);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull DBObject object) {

        Messages.showInfoDialog(project, "Not implemented!", "This feature is not implemented yet.");
    }
}
