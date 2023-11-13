package com.dbn.debugger.jdbc.config;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.debugger.common.config.DBMethodRunConfigFactory;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.debugger.common.config.DBMethodRunConfigType;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.MethodExecutionManager;
import com.dbn.object.DBMethod;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdbcRunConfigFactory extends DBMethodRunConfigFactory<DBMethodRunConfigType, DBMethodJdbcRunConfig> {
    public DBMethodJdbcRunConfigFactory(@NotNull DBMethodRunConfigType type) {
        super(type, DBDebuggerType.JDBC);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DBMethodJdbcRunConfig(project, this, "", DBRunConfigCategory.TEMPLATE);
    }

    @Override
    public DBMethodJdbcRunConfig createConfiguration(Project project, String name, DBRunConfigCategory category) {
        return new DBMethodJdbcRunConfig(project, this, name, category);
    }

    @Override
    public DBMethodJdbcRunConfig createConfiguration(DBMethod method) {
        Project project = method.getProject();
        ExecutionConfigManager executionConfigManager = ExecutionConfigManager.getInstance(project);
        String name = executionConfigManager.createMethodConfigurationName(method);

        DBMethodJdbcRunConfig runConfiguration = new DBMethodJdbcRunConfig(project, this, name, DBRunConfigCategory.CUSTOM);
        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        MethodExecutionInput executionInput = executionManager.getExecutionInput(method);
        runConfiguration.setExecutionInput(executionInput);
        return runConfiguration;
    }
}
