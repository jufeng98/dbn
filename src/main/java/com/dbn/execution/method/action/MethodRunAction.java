package com.dbn.execution.method.action;

import com.dbn.common.icon.Icons;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.method.MethodExecutionManager;
import com.dbn.object.DBMethod;
import com.dbn.object.DBProgram;
import com.dbn.object.action.AnObjectAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MethodRunAction extends AnObjectAction<DBMethod> {
    public MethodRunAction(DBMethod method) {
        super("Run...", Icons.METHOD_EXECUTION_RUN, method);
    }

    MethodRunAction(DBProgram program, DBMethod method) {
        super(method);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull DBMethod object) {

        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        executionManager.startMethodExecution(object, DBDebuggerType.NONE);
    }
}
