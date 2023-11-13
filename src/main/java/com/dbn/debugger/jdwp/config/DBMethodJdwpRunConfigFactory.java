package com.dbn.debugger.jdwp.config;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.debugger.common.config.DBMethodRunConfigFactory;
import com.dbn.debugger.common.config.DBMethodRunConfigType;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.MethodExecutionManager;
import com.dbn.object.DBMethod;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdwpRunConfigFactory extends DBMethodRunConfigFactory<DBMethodRunConfigType, DBMethodJdwpRunConfig> {
    public DBMethodJdwpRunConfigFactory(@NotNull DBMethodRunConfigType type) {
        super(type, DBDebuggerType.JDWP);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DBMethodJdwpRunConfig(project, this, "", DBRunConfigCategory.TEMPLATE);
    }

    @Override
    public DBMethodJdwpRunConfig createConfiguration(Project project, String name, DBRunConfigCategory category) {
        return new DBMethodJdwpRunConfig(project, this, name, category);
    }

    @Override
    public DBMethodJdwpRunConfig createConfiguration(DBMethod method) {
        Project project = method.getProject();
        ExecutionConfigManager executionConfigManager = ExecutionConfigManager.getInstance(project);
        String name = executionConfigManager.createMethodConfigurationName(method);
        name = name + " (JDWP)";
        DBMethodJdwpRunConfig runConfiguration = new DBMethodJdwpRunConfig(project, this, name, DBRunConfigCategory.CUSTOM);
        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        MethodExecutionInput executionInput = executionManager.getExecutionInput(method);
        runConfiguration.setExecutionInput(executionInput);
        return runConfiguration;
    }

    @NotNull
    @Override
    public String getName() {
        return super.getName() + " (JDWP)";
    }
}
