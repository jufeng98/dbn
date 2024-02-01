package com.dbn.debugger.common.config;

import com.dbn.debugger.DBDebuggerType;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.project.DumbAware;

public abstract class DBRunConfigType<T extends DBRunConfigFactory> implements ConfigurationType, DumbAware {
    @Override
    public abstract T[] getConfigurationFactories();
    public abstract String getDefaultRunnerName();
    public abstract T getConfigurationFactory(DBDebuggerType debuggerType);
}
