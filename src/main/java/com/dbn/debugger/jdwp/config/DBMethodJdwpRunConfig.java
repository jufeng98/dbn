package com.dbn.debugger.jdwp.config;

import com.dbn.debugger.common.config.DBMethodRunConfig;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.debugger.common.config.ui.DBMethodRunConfigEditor;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class DBMethodJdwpRunConfig extends DBMethodRunConfig implements DBJdwpRunConfig{

    public DBMethodJdwpRunConfig(Project project, DBMethodJdwpRunConfigFactory factory, String name, DBRunConfigCategory category) {
        super(project, factory, name, category);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new DBMethodRunConfigEditor(this);
    }

    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        return new DBMethodJdwpRunProfileState(env);
    }

}
