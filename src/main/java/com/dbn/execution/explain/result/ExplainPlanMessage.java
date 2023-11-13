package com.dbn.execution.explain.result;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.message.MessageType;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.execution.common.message.ConsoleMessage;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class ExplainPlanMessage extends ConsoleMessage {

    @Getter
    private final ExplainPlanResult explainPlanResult;

    public ExplainPlanMessage(ExplainPlanResult explainPlanResult, MessageType messageType) {
        super(messageType, explainPlanResult.getErrorMessage());
        this.explainPlanResult = explainPlanResult;

        Disposer.register(this, explainPlanResult);
    }

    @Nullable
    @Override
    public ConnectionId getConnectionId() {
        return explainPlanResult.getConnectionId();
    }

    public VirtualFile getVirtualFile() {
        return explainPlanResult.getVirtualFile();
    }

    @Deprecated
    public void navigateToEditor(boolean requestFocus) {
        //executionResult.getExecutionProcessor().navigateToEditor(requestFocus);
    }

    public ConnectionHandler getConnection() {
        return explainPlanResult.getConnection();
    }
}
