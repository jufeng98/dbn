package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.common.message.ui.tree.MessagesTreeRootNode;
import com.dbn.execution.method.MethodExecutionMessage;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class MethodExecutionMessagesNode extends MessagesTreeBundleNode<MessagesTreeRootNode, MethodExecutionMessagesObjectNode> {
    public MethodExecutionMessagesNode(MessagesTreeRootNode parent) {
        super(parent);
    }

    private MethodExecutionMessagesObjectNode getChildTreeNode(VirtualFile virtualFile) {
        for (MethodExecutionMessagesObjectNode messagesTreeNode : getChildren()) {
            if (messagesTreeNode.getFile().equals(virtualFile)) {
                return messagesTreeNode;
            }
        }
        return null;
    }

    public TreePath addExecutionMessage(MethodExecutionMessage executionMessage) {
        MethodExecutionMessagesObjectNode objectNode = getChildTreeNode(executionMessage.getContentFile());
        if (objectNode == null) {
            objectNode = new MethodExecutionMessagesObjectNode(this, executionMessage.getDatabaseFile());
            addChild(objectNode);
            getTreeModel().notifyTreeModelListeners(this, TreeEventType.STRUCTURE_CHANGED);
        }
        return objectNode.addCompilerMessage(executionMessage);
    }

    @Nullable
    public TreePath getTreePath(MethodExecutionMessage executionMessage) {
        DBEditableObjectVirtualFile databaseFile = executionMessage.getDatabaseFile();
        MethodExecutionMessagesObjectNode objectNode = getChildTreeNode(databaseFile);
        return objectNode == null ? null : objectNode.getTreePath(executionMessage);
    }
}