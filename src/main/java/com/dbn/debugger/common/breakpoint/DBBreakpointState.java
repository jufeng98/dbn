package com.dbn.debugger.common.breakpoint;

public class DBBreakpointState {
    private boolean enabled;

    public DBBreakpointState() {
        enabled = true;
    }

    public DBBreakpointState(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
