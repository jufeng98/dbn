package com.dbn.execution.common.message.ui.tree.node;

import com.dbn.common.util.Safe;
import com.dbn.execution.common.message.ui.tree.MessagesTreeLeafNode;
import com.dbn.execution.statement.StatementExecutionMessage;

public class StatementExecutionMessageNode extends MessagesTreeLeafNode<StatementExecutionMessagesFileNode, StatementExecutionMessage> {

    StatementExecutionMessageNode(StatementExecutionMessagesFileNode parent, StatementExecutionMessage executionMessage) {
        super(parent, executionMessage);
    }

    @Override
    public String toString() {
        StatementExecutionMessage executionMessage = getMessage();
        return
            executionMessage.getText() +
            Safe.call(executionMessage.getDatabaseMessage(), dm -> " " + dm.getTitle(), "") + " - Connection: " +
            executionMessage.getExecutionResult().getConnection().getName() + ": " +
            executionMessage.getExecutionResult().getExecutionDuration() + "ms";
    }
}
