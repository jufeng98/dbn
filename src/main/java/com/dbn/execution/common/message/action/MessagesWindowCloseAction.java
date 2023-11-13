package com.dbn.execution.common.message.action;

import com.dbn.common.icon.Icons;
import com.dbn.execution.ExecutionManager;
import com.dbn.execution.common.message.ui.tree.MessagesTree;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MessagesWindowCloseAction extends AbstractExecutionMessagesAction {
    public MessagesWindowCloseAction(MessagesTree messagesTree) {
        super(messagesTree, "Close", Icons.EXEC_RESULT_CLOSE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull MessagesTree messagesTree) {
        ExecutionManager executionManager = ExecutionManager.getInstance(project);
        executionManager.removeMessagesTab();
    }
}
