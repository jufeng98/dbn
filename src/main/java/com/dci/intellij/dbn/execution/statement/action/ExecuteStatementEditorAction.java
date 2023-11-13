package com.dci.intellij.dbn.execution.statement.action;

import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.shortcut.OverridingShortcutInterceptor;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.execution.statement.StatementExecutionManager;
import com.dci.intellij.dbn.language.common.DBLanguagePsiFile;
import com.dci.intellij.dbn.language.common.psi.PsiUtil;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;

public class ExecuteStatementEditorAction extends ProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        Editor editor = Lookups.getEditor(e);
        if (editor != null) {
            FileEditor fileEditor = Editors.getFileEditor(editor);
            if (fileEditor != null) {
                StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
                executionManager.executeStatementAtCursor(fileEditor);
            }
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(isEnabled(e));
        presentation.setIcon(Icons.STMT_EXECUTION_RUN);
        presentation.setText("Execute Statement");
        presentation.setVisible(isVisible(e));
    }

    private static boolean isEnabled(AnActionEvent e) {
        Project project = Lookups.getProject(e);
        if (isNotValid(project)) return false;

        Editor editor = Lookups.getEditor(e);
        if (isNotValid(editor)) return false;

        PsiFile psiFile = PsiUtil.getPsiFile(project, editor.getDocument());
        if (!(psiFile instanceof DBLanguagePsiFile)) return false;

        VirtualFile virtualFile = psiFile.getVirtualFile();
        if (virtualFile instanceof DBSourceCodeVirtualFile) return false;

        return true;
    }

    public static boolean isVisible(AnActionEvent e) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        return !DatabaseDebuggerManager.isDebugConsole(virtualFile);
    }

    /**
     * Ctrl-S override
     */
    public static class ShortcutInterceptor extends OverridingShortcutInterceptor {
        public ShortcutInterceptor() {
            super("DBNavigator.Actions.Execute");
        }

        @Override
        protected boolean canDelegateExecute(AnActionEvent e) {
            return isEnabled(e);
        }
    }
}
