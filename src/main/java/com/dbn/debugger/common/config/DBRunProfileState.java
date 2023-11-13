package com.dbn.debugger.common.config;

import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;

public abstract class DBRunProfileState implements RunProfileState {
    private final ExecutionEnvironment environment;

    public DBRunProfileState(ExecutionEnvironment environment) {
        this.environment = environment;
    }

    public ExecutionEnvironment getEnvironment() {
        return environment;
    }
}
