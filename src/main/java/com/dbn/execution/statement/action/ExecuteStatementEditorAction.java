package com.dbn.execution.statement.action;

import com.dbn.common.action.Lookups;
import com.dbn.common.action.ProjectAction;
import com.dbn.common.icon.Icons;
import com.dbn.common.ui.shortcut.OverridingShortcutInterceptor;
import com.dbn.common.util.Editors;
import com.dbn.debugger.DatabaseDebuggerManager;
import com.dbn.execution.statement.StatementExecutionManager;
import com.dbn.language.common.DBLanguagePsiFile;
import com.dbn.language.common.psi.PsiUtil;
import com.dbn.sql.parser.SqlFile;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.dbn.common.dispose.Checks.isNotValid;

public class ExecuteStatementEditorAction extends ProjectAction {
    private static volatile boolean lock = false;

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        try {
            if (lock) {
                return;
            }
            lock = true;

            Editor editor = Lookups.getEditor(e);
            if (isNotValid(editor)) return;

            FileEditor fileEditor = Editors.getFileEditor(editor);
            if (isNotValid(fileEditor)) return;

            StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
            executionManager.executeStatementAtCursor(fileEditor);
        } finally {
            CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ignored) {
                }
                lock = false;
            });
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
        if (!(psiFile instanceof DBLanguagePsiFile || psiFile instanceof SqlFile)) return false;

        VirtualFile virtualFile = psiFile.getVirtualFile();
        return !(virtualFile instanceof DBSourceCodeVirtualFile);
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
