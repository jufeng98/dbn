package com.dbn.debugger.jdbc.config;

import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.debugger.common.config.DBStatementRunConfig;
import com.dbn.debugger.common.config.ui.DBStatementRunConfigEditor;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBStatementJdbcRunConfig extends DBStatementRunConfig {

    public DBStatementJdbcRunConfig(Project project, DBStatementJdbcRunConfigFactory factory, String name, DBRunConfigCategory category) {
        super(project, factory, name, category);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new DBStatementRunConfigEditor(this);
    }

    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        return new DBStatementJdbcRunProfileState(env);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
/*        if (executionInput == null) {
            throw new RuntimeConfigurationError("No or invalid method selected. The database connection is down, obsolete or method has been dropped.");
        }

        if (executionInput.isObsolete()) {
            throw new RuntimeConfigurationError(
                    "Method " + executionInput.getMethodRef().getQualifiedName() + " could not be resolved. " +
                    "The database connection is down or method has been dropped.");
        }

        ConnectionHandler connection = getMethod().getCache();
        if (!DatabaseFeature.DEBUGGING.isSupported(connection)){
            throw new RuntimeConfigurationError(
                    "Debugging is not supported for " + connection.getDatabaseType().getDisplayName() +" databases.");
        }*/
    }
}
