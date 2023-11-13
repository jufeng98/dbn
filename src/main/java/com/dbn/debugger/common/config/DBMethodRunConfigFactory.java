package com.dbn.debugger.common.config;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.object.DBMethod;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.execution.configurations.RunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class DBMethodRunConfigFactory<T extends DBMethodRunConfigType, C extends DBMethodRunConfig> extends DBRunConfigFactory<T, C> {
    protected DBMethodRunConfigFactory(T type, DBDebuggerType debuggerType) {
        super(type, debuggerType);
    }

    @Override
    public Icon getIcon(@NotNull RunConfiguration configuration) {
        C runConfiguration = (C) configuration;
        MethodExecutionInput executionInput = runConfiguration.getExecutionInput();
        if (executionInput == null || runConfiguration.getCategory() != DBRunConfigCategory.CUSTOM) {
            return getIcon();
        } else {
            DBObjectRef<DBMethod> methodRef = executionInput.getMethodRef();
            DBMethod method = methodRef.get();
            return method == null ? methodRef.getObjectType().getIcon() : method.getIcon();
        }
    }

    public abstract C createConfiguration(DBMethod method);
}
