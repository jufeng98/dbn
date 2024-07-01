package com.dbn.debugger.options;

import com.dbn.common.option.InteractiveOption;
import com.dbn.debugger.DBDebuggerType;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
public enum DebuggerTypeOption implements InteractiveOption {
    JDBC(nls("cfg.debugger.const.DebuggerTypeOption_JDBC"), DBDebuggerType.JDBC),
    JDWP(nls("cfg.debugger.const.DebuggerTypeOption_JDWP"), DBDebuggerType.JDWP),
    ASK(nls("cfg.debugger.const.DebuggerTypeOption_ASK")),
    CANCEL(nls("cfg.debugger.const.DebuggerTypeOption_CANCEL"));

    private final String name;
    private final DBDebuggerType debuggerType;

    DebuggerTypeOption(String name) {
        this.name = name;
        this.debuggerType = null;
    }

    DebuggerTypeOption(String name, DBDebuggerType debuggerType) {
        this.name = name;
        this.debuggerType = debuggerType;
    }

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
