package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.file.VirtualFileRef;
import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.common.ui.tree.Trees;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.statement.StatementExecutionMessage;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class StatementExecutionMessagesFileNode extends MessagesTreeBundleNode<StatementExecutionMessagesNode, StatementExecutionMessageNode> {
    private final VirtualFileRef file;

    StatementExecutionMessagesFileNode(StatementExecutionMessagesNode parent, VirtualFile file) {
        super(parent);
        this.file = VirtualFileRef.of(file);
    }

    @NotNull
    @Override
    public VirtualFile getFile() {
        return VirtualFileRef.ensure(file);
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
