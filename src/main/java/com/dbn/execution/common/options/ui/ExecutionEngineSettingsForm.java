package com.dbn.execution.common.options.ui;

import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ExecutionEngineSettingsForm extends CompositeConfigurationEditorForm<ExecutionEngineSettings> {
    private JPanel mainPanel;
    private JPanel statementExecutionPanel;
    private JPanel methodExecutionPanel;
    private JPanel scriptExecutionPanel;

    public ExecutionEngineSettingsForm(ExecutionEngineSettings settings) {
        super(settings);
        statementExecutionPanel.add(settings.getStatementExecutionSettings().createComponent(), BorderLayout.CENTER);
        methodExecutionPanel.add(settings.getMethodExecutionSettings().createComponent(), BorderLayout.CENTER);
        scriptExecutionPanel.add(settings.getScriptExecutionSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
