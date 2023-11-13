package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.execution.method.MethodExecutionMessage;
import com.dbn.execution.common.message.ui.tree.MessagesTreeLeafNode;

class MethodExecutionMessageNode extends MessagesTreeLeafNode<MethodExecutionMessagesObjectNode, MethodExecutionMessage> {

    MethodExecutionMessageNode(MethodExecutionMessagesObjectNode parent, MethodExecutionMessage methodExecutionMessage) {
        super(parent, methodExecutionMessage);
    }
}