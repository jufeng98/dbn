package com.dbn.debugger.jdbc.frame;

import com.dbn.common.util.Strings;
import com.dbn.database.common.debug.DebuggerRuntimeInfo;
import com.dbn.database.common.debug.ExecutionBacktraceInfo;
import com.dbn.debugger.jdbc.DBJdbcDebugProcess;
import com.dbn.execution.statement.StatementExecutionInput;
import com.intellij.xdebugger.frame.XExecutionStack;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DBJdbcDebugExecutionStack extends XExecutionStack {
    private final DBJdbcDebugStackFrame topFrame;
    private final DBJdbcDebugProcess debugProcess;

    DBJdbcDebugExecutionStack(DBJdbcDebugProcess debugProcess) {
        // WORKAROUND hide the single value "threads" dropdown
        // super(debugProcess.getName(), debugProcess.getIcon());
        super("", null);

        this.debugProcess = debugProcess;
        ExecutionBacktraceInfo backtraceInfo = debugProcess.getBacktraceInfo();
        int frameNumber = backtraceInfo == null ? 1 : backtraceInfo.getTopFrameIndex();
        topFrame = new DBJdbcDebugStackFrame(debugProcess, debugProcess.getRuntimeInfo(), frameNumber);

    }


    @Override
    public void computeStackFrames(int firstFrameIndex, XStackFrameContainer container) {
        List<DBJdbcDebugStackFrame> frames = new ArrayList<>();
        ExecutionBacktraceInfo backtraceInfo = debugProcess.getBacktraceInfo();
        if (backtraceInfo == null) return;

        for (DebuggerRuntimeInfo runtimeInfo : backtraceInfo.getFrames()) {
            if (Strings.isNotEmpty(runtimeInfo.getOwnerName()) || debugProcess.getExecutionInput() instanceof StatementExecutionInput) {
                DBJdbcDebugStackFrame frame = new DBJdbcDebugStackFrame(debugProcess, runtimeInfo, runtimeInfo.getFrameIndex());
                frames.add(frame);
            }
        }
        container.addStackFrames(frames, true) ;
    }
}
