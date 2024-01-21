package com.dbn.debugger.jdwp;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.process.DBProgramRunner;
import com.dbn.debugger.common.state.DBRunProfileState;
import com.intellij.debugger.engine.RemoteDebugProcessHandler;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import com.sun.jdi.Location;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
@UtilityClass
public final class DBJdwpDebugUtil {

    @Nullable
    public static String getOwnerName(@Nullable Location location) {
        try {
            if (location != null) {
                String sourceUrl = location.sourcePath();
                DBJdwpSourcePath sourcePath = DBJdwpSourcePath.from(sourceUrl);
                return sourcePath.getProgramOwner();
            }
        } catch (Exception e) {
            conditionallyLog(e);
            log.error("Failed to resolve owner name", e);
        }

        return null;
    }

    public static ExecutionResult execute(DBRunProfileState state, Executor executor, @NotNull ProgramRunner<?> runner) throws ExecutionException {
        if (runner instanceof DBProgramRunner) {
            DBProgramRunner<?> programRunner = (DBProgramRunner<?>) runner;
            if (programRunner.getDebuggerType() == DBDebuggerType.JDWP) {
                Project project = state.getEnvironment().getProject();
                RemoteDebugProcessHandler processHandler = new RemoteDebugProcessHandler(project);
                return new DefaultExecutionResult(null, processHandler);
            }
        }
        return null;
    }
}
