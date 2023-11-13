package com.dbn.editor.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SessionIdentifier {
    private final Object sessionId;
    private final Object serialNumber;

    public SessionIdentifier(Object sessionId, Object serialNumber) {
        this.sessionId = sessionId;
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return sessionId.toString();
    }
}
