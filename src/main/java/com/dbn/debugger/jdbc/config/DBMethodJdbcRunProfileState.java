package com.dbn.debugger.jdbc.config;

import com.dbn.debugger.common.config.DBRunProfileState;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import org.jetbrains.annotations.NotNull;


public class DBMethodJdbcRunProfileState extends DBRunProfileState {
    public DBMethodJdbcRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        return null;
    }

    public RunnerSettings getRunnerSettings() {
        return null;
    }

    public ConfigurationPerRunnerSettings getConfigurationSettings() {
        return null;
    }
}
