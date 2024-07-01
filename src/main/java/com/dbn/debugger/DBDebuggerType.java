package com.dbn.debugger;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;
import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum DBDebuggerType implements Presentable {
    JDBC(nls("app.debugger.const.DBDebuggerType_JDBC")),
    JDWP(nls("app.debugger.const.DBDebuggerType_JDWP")),
    NONE(nls("app.debugger.const.DBDebuggerType_NONE"));

    private final String name;

    public boolean isDebug() {
        return this != NONE;
    }

    public boolean isSupported() {
        switch (this) {
            case JDWP: {
                try {
                    Class.forName("com.intellij.debugger.engine.JavaStackFrame");
                    Class.forName("com.intellij.debugger.PositionManagerFactory");
                    return true;
                } catch (ClassNotFoundException e) {
                    conditionallyLog(e);
                    return false;
                }
            }
            case JDBC: return true;
            case NONE: return true;
        }
        return false;
    }
}
