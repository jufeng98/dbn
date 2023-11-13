package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.common.ui.tree.Trees;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.statement.StatementExecutionMessage;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class StatementExecutionMessagesFileNode extends MessagesTreeBundleNode<StatementExecutionMessagesNode, StatementExecutionMessageNode> {
    private VirtualFile virtualFile;

    StatementExecutionMessagesFileNode(StatementExecutionMessagesNode parent, VirtualFile virtualFile) {
        super(parent);
        this.virtualFile = virtualFile;
    }

    @NotNull
    @Override
    public VirtualFile getVirtualFile() {
        return Failsafe.nn(virtualFile);
    }

    TreePath addExecutionMessage(StatementExecutionMessage executionMessage) {
        StatementExecutionMessageNode execMessageNode = new StatementExecutionMessageNode(this, executionMessage);
        addChild(execMessageNode);
        getTreeModel().notifyTreeModelListeners(this, TreeEventType.STRUCTURE_CHANGED);
        return Trees.createTreePath(execMessageNode);
    }

    @Nullable
    public TreePath getTreePath(StatementExecutionMessage executionMessage) {
        for (StatementExecutionMessageNode messageNode : getChildren()) {
            if (messageNode.getMessage() == executionMessage) {
                return Trees.createTreePath(messageNode);
            }
        }
        return null;
    }
}
