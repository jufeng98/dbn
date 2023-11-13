package com.dbn.execution.common.message.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.ui.tree.Trees;
import com.dbn.execution.common.message.ui.tree.MessagesTree;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MessagesTreeCollapseAction extends AbstractExecutionMessagesAction {

    public MessagesTreeCollapseAction(MessagesTree messagesTree) {
        super(messagesTree, "Collapse All", Icons.ACTION_COLLAPSE_ALL);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull MessagesTree messagesTree) {
        Trees.collapseAll(messagesTree);
    }
}