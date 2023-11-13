package com.dbn.execution.method.ui;

import com.dbn.common.Pair;
import com.dbn.common.icon.Icons;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.common.ui.dialog.DBNDialogRegistry;
import com.dbn.common.util.Dialogs;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.method.MethodExecutionInput;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MethodExecutionInputDialog extends DBNDialog<MethodExecutionInputForm> {
    private final MethodExecutionInput executionInput;
    private final DBDebuggerType debuggerType;
    private final Runnable executor;

    private MethodExecutionInputDialog(@NotNull MethodExecutionInput executionInput, @NotNull DBDebuggerType debuggerType, @NotNull Runnable executor) {
        super(executionInput.getProject(), (debuggerType.isDebug() ? "Debug" : "Execute") + " method", true);
        this.executionInput = executionInput;
        this.debuggerType = debuggerType;
        this.executor = executor;
        setModal(false);
        setResizable(true);
        setDefaultSize(800, 600);
        init();
    }

    public static void open(@NotNull MethodExecutionInput executionInput, @NotNull DBDebuggerType debuggerType, @NotNull Runnable executor) {
        Dispatch.run(true, () -> {
            val key = Pair.of(executionInput.getMethodRef(), debuggerType);
            Dialogs.show(() -> DBNDialogRegistry.ensure(key, () -> new MethodExecutionInputDialog(executionInput, debuggerType, executor)));
        });
    }

    @NotNull
    @Override
    protected MethodExecutionInputForm createForm() {
        return new MethodExecutionInputForm(this, executionInput, true, debuggerType);
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                new ExecuteAction(),
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    private class ExecuteAction extends AbstractAction {
        ExecuteAction() {
            super(debuggerType.isDebug() ? "Debug" : "Execute",
                    debuggerType.isDebug() ? Icons.METHOD_EXECUTION_DEBUG : Icons.METHOD_EXECUTION_RUN);
            makeFocusAction(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getForm().updateExecutionInput();
                executor.run();
            } finally {
                doOKAction();
            }
        }
    }
}
