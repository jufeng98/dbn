package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.connection.operation.options.OperationSettings;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.execution.compiler.options.CompilerSettings;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DebugMethodAction extends AbstractSourceCodeEditorAction {
    public DebugMethodAction() {
        super("Run Method", "", Icons.METHOD_EXECUTION_DEBUG);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DBSourceCodeVirtualFile sourceCodeFile = getSourcecodeFile(e);
        FileEditor fileEditor = getFileEditor(e);
        if (sourceCodeFile != null && fileEditor != null) {
            Project project = sourceCodeFile.getProject();
            if (project != null) {
                DBMethod method = (DBMethod) sourceCodeFile.getObject();
                DatabaseDebuggerManager debuggerManager = DatabaseDebuggerManager.getInstance(project);
                debuggerManager.startMethodDebugger(method);
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        DBSourceCodeVirtualFile sourceCodeFile = getSourcecodeFile(e);
        Presentation presentation = e.getPresentation();
        boolean visible = false;
        if (sourceCodeFile != null) {
            DBSchemaObject schemaObject = sourceCodeFile.getObject();
            if (schemaObject.getObjectType().matches(DBObjectType.METHOD) && DatabaseFeature.DEBUGGING.isSupported(schemaObject)) {
                visible = true;
            }
        }

        presentation.setVisible(visible);
        presentation.setText("Debug Method");
    }

    private static CompilerSettings getCompilerSettings(Project project) {
        return OperationSettings.getInstance(project).getCompilerSettings();
    }
}
