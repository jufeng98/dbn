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

public class ObjectEditDataAction extends ProjectAction {
    private final DBObjectRef<DBSchemaObject> object;

    public ObjectEditDataAction(DBSchemaObject object) {
        super("Edit Data", null, Icons.OBEJCT_EDIT_DATA);
        this.object = DBObjectRef.of(object);
        setDefaultIcon(true);
    }

    public DBSchemaObject getObject() {
        return DBObjectRef.ensure(object);
    }

    @Nullable
    @Override
    public Project getProject() {
        return getObject().getProject();
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DBSchemaObject object = getObject();
        DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
        editorManager.connectAndOpenEditor(object, EditorProviderId.DATA, false, true);
    }
}