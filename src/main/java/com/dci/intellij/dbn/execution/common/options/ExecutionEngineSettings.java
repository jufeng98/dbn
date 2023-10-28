package com.dci.intellij.dbn.execution.common.options;

import com.dci.intellij.dbn.common.options.CompositeProjectConfiguration;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.execution.ExecutionTarget;
import com.dci.intellij.dbn.execution.common.options.ui.ExecutionEngineSettingsForm;
import com.dci.intellij.dbn.execution.method.options.MethodExecutionSettings;
import com.dci.intellij.dbn.execution.script.options.ScriptExecutionSettings;
import com.dci.intellij.dbn.execution.statement.options.StatementExecutionSettings;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettings;
import com.dci.intellij.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ExecutionEngineSettings extends CompositeProjectConfiguration<ProjectSettings, ExecutionEngineSettingsForm> implements TopLevelConfig {
    private final StatementExecutionSettings statementExecutionSettings = new StatementExecutionSettings(this);
    private final ScriptExecutionSettings scriptExecutionSettings       = new ScriptExecutionSettings(this);
    private final MethodExecutionSettings methodExecutionSettings       = new MethodExecutionSettings(this);

    public ExecutionEngineSettings(ProjectSettings parent) {
        super(parent);
    }

    public static ExecutionEngineSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getExecutionEngineSettings();
    }

    @NotNull
    public ExecutionTimeoutSettings getExecutionTimeoutSettings(@NotNull ExecutionTarget executionTarget) {
        switch (executionTarget) {
            case STATEMENT: return getStatementExecutionSettings();
            case SCRIPT: return getScriptExecutionSettings();
            case METHOD: return getMethodExecutionSettings();
        }
        throw new IllegalArgumentException("Invalid execution type " + executionTarget);
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.ExecutionEngineSettings";
    }

    @Override
    public String getDisplayName() {
        return "Execution Engine";
    }

    @Override
    public String getHelpTopic() {
        return "executionEngine";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.EXECUTION_ENGINE;
    }

    @NotNull
    @Override
    public ExecutionEngineSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                    Configuration                      *
     *********************************************************/
    @Override
    @NotNull
    public ExecutionEngineSettingsForm createConfigurationEditor() {
        return new ExecutionEngineSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "execution-engine-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                statementExecutionSettings,
                scriptExecutionSettings,
                methodExecutionSettings};
    }
}
