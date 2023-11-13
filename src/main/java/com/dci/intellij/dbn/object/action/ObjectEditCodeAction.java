package com.dci.intellij.dbn.object.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.DatabaseFileEditorManager;
import com.dci.intellij.dbn.editor.EditorProviderId;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectEditCodeAction extends ProjectAction {
    private final DBObjectRef<DBSchemaObject> object;

    ObjectEditCodeAction(DBSchemaObject object) {
        super("Edit Code", null, Icons.OBEJCT_EDIT_SOURCE);
        this.object = DBObjectRef.of(object);
        setDefaultIcon(true);
    }

    @Nullable
    @Override
    public  Project getProject() {
        return getObject().getProject();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DBSchemaObject schemaObject = getObject();
        DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
        editorManager.connectAndOpenEditor(schemaObject, EditorProviderId.CODE, false, true);
    }

    @NotNull
    private DBSchemaObject getObject() {
        return DBObjectRef.ensure(object);
    }
}
