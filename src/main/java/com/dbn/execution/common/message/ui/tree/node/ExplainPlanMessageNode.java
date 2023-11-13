package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.execution.common.message.ui.tree.MessagesTreeLeafNode;
import com.dbn.execution.explain.result.ExplainPlanMessage;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public class ExplainPlanMessageNode extends MessagesTreeLeafNode<ExplainPlanMessagesFileNode, ExplainPlanMessage> {

    ExplainPlanMessageNode(ExplainPlanMessagesFileNode parent, ExplainPlanMessage explainPlanMessage) {
        super(parent, explainPlanMessage);
    }

    @Nullable
    @Override
    public VirtualFile getVirtualFile() {
        return getParent().getVirtualFile();
    }

    @Override
    public String toString() {
        ExplainPlanMessage explainPlanMessage = getMessage();
        return
            explainPlanMessage.getText() + " - Connection: " +
            explainPlanMessage.getConnection().getName();
    }
}
