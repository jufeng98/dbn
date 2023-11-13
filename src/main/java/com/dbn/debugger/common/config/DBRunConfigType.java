package com.dbn.debugger.common.config;

import com.dbn.debugger.DBDebuggerType;
import com.intellij.execution.configurations.ConfigurationType;

public abstract class DBRunConfigType<T extends DBRunConfigFactory> implements ConfigurationType {
    @Override
    public abstract T[] getConfigurationFactories();
    public abstract String getDefaultRunnerName();
    public abstract T getConfigurationFactory(DBDebuggerType debuggerType);

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
