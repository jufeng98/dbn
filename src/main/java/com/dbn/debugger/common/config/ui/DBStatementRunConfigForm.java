package com.dbn.debugger.common.config.ui;

import com.dbn.common.ui.form.DBNHintForm;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.debugger.common.config.DBStatementRunConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DBStatementRunConfigForm extends DBProgramRunConfigForm<DBStatementRunConfig> {
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel hintPanel;

    public DBStatementRunConfigForm(DBStatementRunConfig configuration) {
        super(configuration.getProject(), configuration.getDebuggerType());
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
    public void writeConfiguration(DBStatementRunConfig configuration) {
    }

    @Override
    public void readConfiguration(DBStatementRunConfig configuration) {
    }
}
