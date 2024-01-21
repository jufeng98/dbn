package com.dbn.debugger.common.state;

import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import lombok.Getter;

@Getter
public abstract class DBRunProfileState implements RunProfileState {
    private final ExecutionEnvironment environment;

    public DBRunProfileState(ExecutionEnvironment environment) {
        this.environment = environment;
    }

}
