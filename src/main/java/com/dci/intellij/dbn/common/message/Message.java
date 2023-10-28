package com.dci.intellij.dbn.common.message;

import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
