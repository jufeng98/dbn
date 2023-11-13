package com.dbn.editor.session;

public enum SessionInterruptionType {
    DISCONNECT,
    TERMINATE;

    public String disconnectedAction() {
        return this == SessionInterruptionType.TERMINATE ? "killed" : "disconnected";
    }

    public String disconnectingAction() {
        return this == SessionInterruptionType.TERMINATE ? "killing" : "disconnecting";
    }

    public String taskAction(int sessionCount) {
        return (this == SessionInterruptionType.TERMINATE ? "Killing" : "Disconnecting") + (sessionCount == 1 ? " Session" : " Sessions");
    }
}
