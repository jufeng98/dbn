package com.dbn.execution.method.action;

import com.dbn.common.icon.Icons;
import com.dbn.debugger.DatabaseDebuggerManager;
import com.dbn.object.DBMethod;
import com.dbn.object.DBProgram;
import com.dbn.object.action.AnObjectAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MethodDebugAction extends AnObjectAction<DBMethod> {
    public MethodDebugAction(DBMethod method) {
        super("Debug...", Icons.METHOD_EXECUTION_DEBUG, method);
    }

    MethodDebugAction(DBProgram program, DBMethod method) {
        super(method);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull DBMethod object) {

        DatabaseDebuggerManager executionManager = DatabaseDebuggerManager.getInstance(project);
        executionManager.startMethodDebugger(object);
    }
}
