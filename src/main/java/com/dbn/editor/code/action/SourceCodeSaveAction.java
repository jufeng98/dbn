package com.dbn.editor.code.action;

import com.dbn.common.dispose.Checks;
import com.dbn.common.environment.EnvironmentManager;
import com.dbn.common.icon.Icons;
import com.dbn.common.option.ConfirmationOptionHandler;
import com.dbn.common.ui.shortcut.ComplementaryShortcutInterceptor;
import com.dbn.editor.code.options.CodeEditorConfirmationSettings;
import com.dbn.editor.code.options.CodeEditorSettings;
import com.dbn.editor.DBContentType;
import com.dbn.editor.code.SourceCodeEditor;
import com.dbn.editor.code.SourceCodeManager;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.dispose.Checks.isNotValid;
import static com.dbn.vfs.file.status.DBFileStatus.SAVING;

public class SourceCodeSaveAction extends AbstractCodeEditorAction {
    public SourceCodeSaveAction() {
        super("", null, Icons.CODE_EDITOR_SAVE_TO_DATABASE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull SourceCodeEditor fileEditor, @NotNull DBSourceCodeVirtualFile sourceCodeFile) {
         performSave(project, fileEditor, sourceCodeFile);
    }

    private static void performSave(@NotNull Project project, @NotNull SourceCodeEditor fileEditor, @NotNull DBSourceCodeVirtualFile sourceCodeFile) {
        CodeEditorSettings editorSettings = CodeEditorSettings.getInstance(project);
        CodeEditorConfirmationSettings confirmationSettings = editorSettings.getConfirmationSettings();
        ConfirmationOptionHandler optionHandler = confirmationSettings.getSaveChanges();

        String objectName = fileEditor.getObject().getQualifiedNameWithType();
        boolean canContinue = optionHandler.resolve(objectName);
        if (!canContinue) return;

        SourceCodeManager sourceCodeManager = SourceCodeManager.getInstance(project);
        sourceCodeManager.saveSourceCode(sourceCodeFile, fileEditor, null);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project, @Nullable SourceCodeEditor fileEditor, @Nullable DBSourceCodeVirtualFile sourceCodeFile) {
        Presentation presentation = e.getPresentation();
        if (Checks.isValid(sourceCodeFile)) {
            EnvironmentManager environmentManager = EnvironmentManager.getInstance(project);
            boolean readonly = environmentManager.isReadonly(sourceCodeFile);
            presentation.setVisible(!readonly);
            DBContentType contentType = sourceCodeFile.getContentType();
            String text =
                    contentType == DBContentType.CODE_SPEC ? "Save Spec" :
                    contentType == DBContentType.CODE_BODY ? "Save Body" : "Save";

            presentation.setEnabled(sourceCodeFile.isModified() && sourceCodeFile.isNot(SAVING));
            presentation.setText(text);
        } else {
            presentation.setEnabled(false);
        }
    }

    /**
     * Ctrl-S override
     */
    public static class ShortcutInterceptor extends ComplementaryShortcutInterceptor {
        public ShortcutInterceptor() {
            super("DBNavigator.Actions.SourceEditor.Save");
        }

        @Override
        protected boolean canDelegateExecute(AnActionEvent e) {
            DBSourceCodeVirtualFile sourcecodeFile = getSourcecodeFile(e);
            if (isNotValid(sourcecodeFile)) return false;
            if (!sourcecodeFile.isModified()) return false;
            return true;
        }
    }
}
