package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.common.message.ui.tree.MessagesTreeRootNode;
import com.dbn.execution.explain.result.ExplainPlanMessage;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class ExplainPlanMessagesNode extends MessagesTreeBundleNode<MessagesTreeRootNode, ExplainPlanMessagesFileNode> {
    public ExplainPlanMessagesNode(MessagesTreeRootNode parent) {
        super(parent);
    }

    @Nullable
    private ExplainPlanMessagesFileNode getChildTreeNode(VirtualFile virtualFile) {
        for (ExplainPlanMessagesFileNode messagesTreeNode : getChildren()) {
            if (virtualFile.equals(messagesTreeNode.getFile())) {
                return messagesTreeNode;
            }
        }
        return null;
    }

    public TreePath addExplainPlanMessage(ExplainPlanMessage explainPlanMessage) {
        ExplainPlanMessagesFileNode node = getFileTreeNode(explainPlanMessage);
        if (node == null) {
            node = new ExplainPlanMessagesFileNode(this, explainPlanMessage.getVirtualFile());
            addChild(node);
            getTreeModel().notifyTreeModelListeners(this, TreeEventType.STRUCTURE_CHANGED);
        }
        return node.addExplainPlanMessage(explainPlanMessage);
    }


    @Nullable
    public TreePath getTreePath(ExplainPlanMessage explainPlanMessage) {
        ExplainPlanMessagesFileNode messagesFileNode = getFileTreeNode(explainPlanMessage);
        if (messagesFileNode != null) {
            return messagesFileNode.getTreePath(explainPlanMessage);
        }
        return null;
    }

    @Nullable
    private ExplainPlanMessagesFileNode getFileTreeNode(ExplainPlanMessage explainPlanMessage) {
        return getChildTreeNode(explainPlanMessage.getVirtualFile());
    }
}
