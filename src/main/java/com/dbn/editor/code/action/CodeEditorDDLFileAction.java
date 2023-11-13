package com.dbn.editor.code.action;

import com.dbn.common.action.GroupPopupAction;
import com.dbn.common.action.Lookups;
import com.dbn.common.icon.Icons;
import com.dbn.ddl.action.DDLFileAttachAction;
import com.dbn.ddl.action.DDLFileCreateAction;
import com.dbn.ddl.action.DDLFileDetachAction;
import com.dbn.ddl.action.DDLFileSettingsAction;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class CodeEditorDDLFileAction extends GroupPopupAction {
    public CodeEditorDDLFileAction() {
        super("DDL File", "DDL File", Icons.CODE_EDITOR_DDL_FILE);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        DBSourceCodeVirtualFile sourceCodeFile = getSourcecodeFile(e);
        Presentation presentation = e.getPresentation();
        presentation.setIcon(Icons.CODE_EDITOR_DDL_FILE);
        presentation.setText("DDL Files");
        presentation.setEnabled(sourceCodeFile != null);
    }

    @Override
    protected AnAction[] getActions(AnActionEvent e) {
        DBSourceCodeVirtualFile sourceCodeFile = getSourcecodeFile(e);
        if (sourceCodeFile != null) {
            DBSchemaObject object = sourceCodeFile.getObject();
            return new AnAction[]{
                    new DDLFileCreateAction(object),
                    new DDLFileAttachAction(object),
                    new DDLFileDetachAction(object),
                    new Separator(),
                    new DDLFileSettingsAction()
            };
        }
        return new AnAction[0];
    }

    protected static DBSourceCodeVirtualFile getSourcecodeFile(AnActionEvent e) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        return virtualFile instanceof DBSourceCodeVirtualFile ? (DBSourceCodeVirtualFile) virtualFile : null;
    }

}
