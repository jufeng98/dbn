package com.dbn.debugger.jdbc.config;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.debugger.common.config.DBMethodRunConfig;
import com.dbn.debugger.common.config.DBMethodRunConfigFactory;
import com.dbn.debugger.common.config.DBMethodRunConfigType;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.MethodExecutionManager;
import com.dbn.object.DBMethod;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdbcRunConfigFactory extends DBMethodRunConfigFactory<DBMethodRunConfigType, DBMethodRunConfig> {
    public DBMethodJdbcRunConfigFactory(@NotNull DBMethodRunConfigType type) {
        super(type, DBDebuggerType.JDBC);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DBMethodRunConfig(project, this, "", DBRunConfigCategory.TEMPLATE);
    }

    @Override
    public DBMethodRunConfig createConfiguration(Project project, String name, DBRunConfigCategory category) {
        return new DBMethodRunConfig(project, this, name, category);
    }

    @Override
    public DBMethodRunConfig createConfiguration(DBMethod method) {
        Project project = method.getProject();
        ExecutionConfigManager executionConfigManager = ExecutionConfigManager.getInstance(project);
        String name = executionConfigManager.createMethodConfigurationName(method);

        DBMethodRunConfig runConfiguration = new DBMethodRunConfig(project, this, name, DBRunConfigCategory.CUSTOM);
        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        MethodExecutionInput executionInput = executionManager.getExecutionInput(method);
        runConfiguration.setExecutionInput(executionInput);
        return runConfiguration;
    }
}
