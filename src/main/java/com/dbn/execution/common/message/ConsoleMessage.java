package com.dbn.execution.common.message;

import com.dbn.common.message.Message;
import com.dbn.common.message.MessageType;
import com.dbn.connection.ConnectionId;
import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.Nullable;

public abstract class ConsoleMessage extends Message implements Disposable {
    private boolean isNew = true;

    public ConsoleMessage(MessageType type, String text) {
        super(type, text);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Nullable
    public ConnectionId getConnectionId() {
        return null;
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
