package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.common.ui.tree.Trees;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.explain.result.ExplainPlanMessage;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class ExplainPlanMessagesFileNode extends MessagesTreeBundleNode<ExplainPlanMessagesNode, ExplainPlanMessageNode> {
    private VirtualFile virtualFile;

    ExplainPlanMessagesFileNode(ExplainPlanMessagesNode parent, VirtualFile virtualFile) {
        super(parent);
        this.virtualFile = virtualFile;
    }

    @NotNull
    @Override
    public VirtualFile getVirtualFile() {
        return Failsafe.nn(virtualFile);
    }

    TreePath addExplainPlanMessage(ExplainPlanMessage explainPlanMessage) {
        ExplainPlanMessageNode explainPlanMessageNode = new ExplainPlanMessageNode(this, explainPlanMessage);
        addChild(explainPlanMessageNode);
        getTreeModel().notifyTreeModelListeners(this, TreeEventType.STRUCTURE_CHANGED);
        return Trees.createTreePath(explainPlanMessageNode);
    }

    @Nullable
    public TreePath getTreePath(ExplainPlanMessage explainPlanMessage) {
        for (ExplainPlanMessageNode messageNode : getChildren()) {
            if (messageNode.getMessage() == explainPlanMessage) {
                return Trees.createTreePath(messageNode);
            }
        }
        return null;
    }
}
