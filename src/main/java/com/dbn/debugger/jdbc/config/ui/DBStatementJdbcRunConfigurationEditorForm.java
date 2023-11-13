package com.dbn.debugger.jdbc.config.ui;

import com.dbn.common.ui.form.DBNHintForm;
import com.dbn.debugger.ExecutionConfigManager;
import com.dbn.debugger.common.config.DBRunConfigCategory;
import com.dbn.debugger.common.config.ui.DBProgramRunConfigurationEditorForm;
import com.dbn.debugger.jdbc.config.DBStatementJdbcRunConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DBStatementJdbcRunConfigurationEditorForm extends DBProgramRunConfigurationEditorForm<DBStatementJdbcRunConfig> {
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JCheckBox compileDependenciesCheckBox;
    private JPanel hintPanel;

    public DBStatementJdbcRunConfigurationEditorForm(DBStatementJdbcRunConfig configuration) {
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
    public void writeConfiguration(DBStatementJdbcRunConfig configuration) {
        configuration.setCompileDependencies(compileDependenciesCheckBox.isSelected());
        //selectMethodAction.setConfiguration(configuration);
    }

    @Override
    public void readConfiguration(DBStatementJdbcRunConfig configuration) {
        compileDependenciesCheckBox.setSelected(configuration.isCompileDependencies());
    }
}
