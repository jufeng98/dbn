package com.dbn.common.message;

import com.dbn.common.dispose.StatefulDisposableBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Message extends StatefulDisposableBase {
    protected MessageType type;
    protected String text;

    public Message(MessageType type, String text) {
        this.type = type;
        this.text = text;
    }

    public boolean isError() {
        return type == MessageType.ERROR;
    }

    public boolean isInfo() {
        return type == MessageType.INFO;
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
