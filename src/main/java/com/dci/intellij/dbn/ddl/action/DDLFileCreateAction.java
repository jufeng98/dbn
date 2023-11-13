package com.dci.intellij.dbn.ddl.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.ddl.DDLFileAttachmentManager;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DDLFileCreateAction extends ProjectAction {
    private final DBObjectRef<DBSchemaObject> object;

    public DDLFileCreateAction(DBSchemaObject object) {
        super("Create New...", null, Icons.CODE_EDITOR_DDL_FILE_NEW);
        this.object = DBObjectRef.of(object);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DDLFileAttachmentManager fileAttachmentManager = DDLFileAttachmentManager.getInstance(project);
        fileAttachmentManager.createDDLFile(object);
    }

}
