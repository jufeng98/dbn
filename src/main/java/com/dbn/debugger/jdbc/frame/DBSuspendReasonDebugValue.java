package com.dbn.debugger.jdbc.frame;

import com.dbn.common.icon.Icons;
import com.dbn.database.common.debug.DebuggerRuntimeInfo;
import com.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.intellij.xdebugger.frame.XValueNode;
import com.intellij.xdebugger.frame.XValuePlace;
import org.jetbrains.annotations.NotNull;

public class DBSuspendReasonDebugValue extends DBJdbcDebugValue {
    DBSuspendReasonDebugValue(DBJdbcDebugStackFrame stackFrame) {
        super(stackFrame, null, "DEBUG_RUNTIME_EVENT", null, Icons.EXEC_MESSAGES_INFO);
    }

    @Override
    public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {
        DebuggerRuntimeInfo runtimeInfo = getDebugProcess().getRuntimeInfo();
        String reason = "Unknown";
        if (runtimeInfo != null) {
            DatabaseDebuggerInterface debuggerInterface = getDebugProcess().getDebuggerInterface();
            reason = runtimeInfo.getReason() +" (" + debuggerInterface.getRuntimeEventReason(runtimeInfo.getReason()) + ")";
        }
        node.setPresentation(Icons.EXEC_MESSAGES_INFO, null, reason, false);
    }
}
