package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.file.VirtualFileRef;
import com.dbn.common.ui.tree.TreeEventType;
import com.dbn.common.ui.tree.Trees;
import com.dbn.execution.common.message.ui.tree.MessagesTreeBundleNode;
import com.dbn.execution.explain.result.ExplainPlanMessage;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;

public class ExplainPlanMessagesFileNode extends MessagesTreeBundleNode<ExplainPlanMessagesNode, ExplainPlanMessageNode> {
    private final VirtualFileRef file;

    ExplainPlanMessagesFileNode(ExplainPlanMessagesNode parent, VirtualFile file) {
        super(parent);
        this.file = VirtualFileRef.of(file);
    }

    @NotNull
    @Override
    public VirtualFile getFile() {
        return VirtualFileRef.ensure(file);
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
