package com.dbn.ddl.action;

import com.dbn.ddl.DDLFileAttachmentManager;
import com.dbn.object.action.AnObjectAction;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DDLFileAttachAction extends AnObjectAction<DBSchemaObject> {
    public DDLFileAttachAction(@NotNull DBSchemaObject object) {
        super("Attach files", null, object);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DBSchemaObject target) {
        DDLFileAttachmentManager fileAttachmentManager = DDLFileAttachmentManager.getInstance(project);
        fileAttachmentManager.attachDDLFiles(target.ref());
    }

}
