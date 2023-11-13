package com.dbn.debugger.jdwp.config.ui;

import com.dbn.common.ui.form.DBNHintForm;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.debugger.common.config.ui.DBProgramRunConfigurationEditorForm;
import com.dbn.debugger.jdwp.config.DBStatementJdwpRunConfig;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DBStatementJdwpRunConfigEditorForm extends DBProgramRunConfigurationEditorForm<DBStatementJdwpRunConfig> {
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel hintPanel;

    public DBStatementJdwpRunConfigEditorForm(DBStatementJdwpRunConfig configuration) {
        super(configuration.getProject());
        if (configuration.getCategory() != DBRunConfigCategory.CUSTOM) {
            headerPanel.setVisible(false);
            DBNHintForm hintForm = new DBNHintForm(this, ExecutionConfigManager.GENERIC_STATEMENT_RUNNER_HINT, null, true);
            hintPanel.setVisible(true);
            hintPanel.add(hintForm.getComponent());
        } else {
            hintPanel.setVisible(false);
        }
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void writeConfiguration(DBStatementJdwpRunConfig configuration) throws ConfigurationException {}

    @Override
    public void readConfiguration(DBStatementJdwpRunConfig configuration) {}
}
