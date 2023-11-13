package com.dbn.connection.operation.options;

import com.dbn.common.options.CompositeProjectConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.connection.operation.options.ui.OperationsSettingsForm;
import com.dbn.connection.transaction.options.TransactionManagerSettings;
import com.dbn.debugger.options.DebuggerSettings;
import com.dbn.editor.session.options.SessionBrowserSettings;
import com.dbn.execution.compiler.options.CompilerSettings;
import com.dbn.options.ConfigId;
import com.dbn.options.ProjectSettings;
import com.dbn.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class OperationSettings extends CompositeProjectConfiguration<ProjectSettings, OperationsSettingsForm> implements TopLevelConfig {
    private final TransactionManagerSettings transactionManagerSettings = new TransactionManagerSettings(this);
    private final SessionBrowserSettings sessionBrowserSettings         = new SessionBrowserSettings(this);
    private final CompilerSettings compilerSettings                     = new CompilerSettings(this);
    private final DebuggerSettings debuggerSettings                     = new DebuggerSettings(this);


    public OperationSettings(ProjectSettings parent) {
        super(parent);
    }

    public static OperationSettings getInstance(@NotNull Project project) {
        return ProjectSettings.get(project).getOperationSettings();
    }

    @NotNull
    @Override
    public String getId() {
        return "DBNavigator.Project.OperationSettings";
    }

    @Override
    public String getDisplayName() {
        return "Operations";
    }

    @Override
    public String getHelpTopic() {
        return "operations";
    }

    @Override
    public ConfigId getConfigId() {
        return ConfigId.OPERATIONS;
    }

    @NotNull
    @Override
    public OperationSettings getOriginalSettings() {
        return getInstance(getProject());
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    @NotNull
    public OperationsSettingsForm createConfigurationEditor() {
        return new OperationsSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "operation-settings";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                transactionManagerSettings,
                sessionBrowserSettings,
                compilerSettings,
                debuggerSettings};
    }
}
