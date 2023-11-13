package com.dci.intellij.dbn.code.common.intention;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.script.ScriptExecutionManager;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dci.intellij.dbn.common.dispose.Checks.allValid;
import static com.dci.intellij.dbn.common.util.Editors.isMainEditor;
import static com.dci.intellij.dbn.common.util.Files.isDbLanguagePsiFile;
import static com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager.hasConnectivityContext;
import static com.dci.intellij.dbn.debugger.DatabaseDebuggerManager.isDebugConsole;

public class ExecuteScriptIntentionAction extends GenericIntentionAction implements HighPriorityAction {
    @Override
    @NotNull
    public String getText() {
        return "Execute SQL script";
    }


    @Override
    public Icon getIcon(int flags) {
        return Icons.EXECUTE_SQL_SCRIPT;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        if (!isDbLanguagePsiFile(psiFile)) return false;

        VirtualFile file = psiFile.getVirtualFile();
        if (file instanceof VirtualFileWindow) return false;
        if (file instanceof DBSourceCodeVirtualFile) return false;
        if (isDebugConsole(file)) return false;
        if (!hasConnectivityContext(file)) return false;
        if (!isMainEditor(editor)) return false;

        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        if (!allValid(project, editor, psiFile)) return;
        if (!isDbLanguagePsiFile(psiFile)) return;

        FileDocumentManager documentManager = FileDocumentManager.getInstance();
        documentManager.saveDocument(editor.getDocument());
        ScriptExecutionManager scriptExecutionManager = ScriptExecutionManager.getInstance(project);
        scriptExecutionManager.executeScript(psiFile.getVirtualFile());
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }


    @Override
    protected Integer getGroupPriority() {
        return 3;
    }
}
