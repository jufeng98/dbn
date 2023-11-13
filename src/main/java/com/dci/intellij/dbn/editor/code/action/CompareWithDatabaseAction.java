package com.dci.intellij.dbn.editor.code.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.code.SourceCodeEditor;
import com.dci.intellij.dbn.editor.code.diff.SourceCodeDiffManager;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class CompareWithDatabaseAction extends AbstractCodeEditorDiffAction {
    public CompareWithDatabaseAction() {
        super("Compare with database", null, Icons.CODE_EDITOR_DIFF_DB);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull SourceCodeEditor fileEditor,
            @NotNull DBSourceCodeVirtualFile sourceCodeFile) {

    SourceCodeDiffManager diffManager = SourceCodeDiffManager.getInstance(project);
        diffManager.opedDatabaseDiffWindow(sourceCodeFile);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project, @Nullable SourceCodeEditor fileEditor, @Nullable DBSourceCodeVirtualFile sourceCodeFile) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Compare with Database");
        presentation.setEnabled(isValid(fileEditor) && isValid(sourceCodeFile));
    }
}
