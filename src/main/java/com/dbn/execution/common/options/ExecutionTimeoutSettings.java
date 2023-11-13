package com.dbn.execution.common.options;

public interface ExecutionTimeoutSettings {
    int getDebugExecutionTimeout();

    int getExecutionTimeout();

    void setExecutionTimeout(int timeout);

    void setDebugExecutionTimeout(int timeout);
}
