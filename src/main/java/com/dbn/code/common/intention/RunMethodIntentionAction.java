package com.dbn.code.common.intention;

import com.dbn.common.icon.Icons;
import com.dbn.database.DatabaseFeature;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.method.MethodExecutionManager;
import com.dbn.object.DBMethod;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RunMethodIntentionAction extends AbstractMethodExecutionIntentionAction{

    @Override
    protected String getActionName() {
        return "Run method";
    }

    @Override
    public Icon getIcon(int flags) {
        return Icons.METHOD_EXECUTION_RUN;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
        if (psiFile != null) {
            DBMethod method = resolveMethod(editor, psiFile);
            return DatabaseFeature.DEBUGGING.isSupported(method);
        }
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        DBMethod method = resolveMethod(editor, psiFile);
        if (method != null) {
            MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
            executionManager.startMethodExecution(method, DBDebuggerType.NONE);
        }
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

}
