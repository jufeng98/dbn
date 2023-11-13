package com.dbn.execution;

import com.dbn.common.project.ProjectRef;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.execution.common.options.ExecutionTimeoutSettings;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ExecutionTimeout {
    private final ProjectRef project;
    private final ExecutionTarget executionTarget;
    private final boolean debug;
    private int customValue;
    private int settingsValue;

    ExecutionTimeout(@NotNull Project project, ExecutionTarget executionTarget, boolean debug) {
        this.project = ProjectRef.of(project);
        this.executionTarget = executionTarget;
        this.debug = debug;
        this.settingsValue = getSettingsExecutionTimeout();
        this.customValue = getSettingsExecutionTimeout();
    }

    public int get() {
        int timeout = getSettingsExecutionTimeout();
        if (customValue != settingsValue) {
            this.settingsValue = timeout;
        } else {
            this.settingsValue = timeout;
            this.customValue = timeout;
        }
        return customValue;
    }

    public void set(int value) {
        this.customValue = value;
    }

    private int getSettingsExecutionTimeout() {
        Project project = this.project.ensure();
        ExecutionEngineSettings executionEngineSettings = ExecutionEngineSettings.getInstance(project);
        ExecutionTimeoutSettings timeoutSettings = executionEngineSettings.getExecutionTimeoutSettings(executionTarget);
        return debug ?
                timeoutSettings.getDebugExecutionTimeout() :
                timeoutSettings.getExecutionTimeout();
    }

}
