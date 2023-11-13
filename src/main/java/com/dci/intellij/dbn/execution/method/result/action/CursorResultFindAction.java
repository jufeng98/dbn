package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.method.result.ui.MethodExecutionCursorResultForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class CursorResultFindAction extends MethodExecutionCursorResultAction {
    public CursorResultFindAction() {
        super("Find Data", Icons.ACTION_FIND);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        MethodExecutionCursorResultForm cursorResultForm = getCursorResultForm(e);
        if (isValid(cursorResultForm)) {
            cursorResultForm.showSearchHeader();
        }
    }
}
