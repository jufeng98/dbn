package com.dbn.object.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.common.icon.Icons;
import com.dbn.editor.DatabaseFileEditorManager;
import com.dbn.editor.EditorProviderId;
import com.dbn.object.DBView;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenViewDataAction extends ProjectAction {
    private final DBObjectRef<DBView> view;

    public OpenViewDataAction(DBView view) {
        super("View Data", null, Icons.OBEJCT_VIEW_DATA);
        this.view = DBObjectRef.of(view);
        setDefaultIcon(true);
    }

    public DBView getView() {
        return DBObjectRef.ensure(view);
    }

    @Nullable
    @Override
    public  Project getProject() {
        return super.getProject();
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
        editorManager.connectAndOpenEditor(getView(), EditorProviderId.DATA, false, true);
    }
}