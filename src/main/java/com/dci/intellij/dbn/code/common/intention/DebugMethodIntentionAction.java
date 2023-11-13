package com.dci.intellij.dbn.code.common.intention;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.object.DBMethod;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DebugMethodIntentionAction extends AbstractMethodExecutionIntentionAction {

    @Override
    protected String getActionName() {
        return "Debug method";
    }

    @Override
    public Icon getIcon(int flags) {
        return Icons.METHOD_EXECUTION_DEBUG;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        if (psiFile != null) {
            DBMethod method = resolveMethod(editor, psiFile);
            return method != null;
        }
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        DBMethod method = resolveMethod(editor, psiFile);
        if (method != null) {
            DatabaseDebuggerManager executionManager = DatabaseDebuggerManager.getInstance(project);
            executionManager.startMethodDebugger(method);
        }
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    protected Integer getGroupPriority() {
        return 1;
    }
}
