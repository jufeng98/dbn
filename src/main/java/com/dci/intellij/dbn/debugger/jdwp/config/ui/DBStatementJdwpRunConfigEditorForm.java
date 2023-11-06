package com.dci.intellij.dbn.debugger.jdwp.config.ui;

import com.dci.intellij.dbn.common.ui.form.DBNHintForm;
import com.dci.intellij.dbn.debugger.ExecutionConfigManager;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfigCategory;
import com.dci.intellij.dbn.debugger.common.config.ui.DBProgramRunConfigurationEditorForm;
import com.dci.intellij.dbn.debugger.jdwp.config.DBStatementJdwpRunConfig;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DBStatementJdwpRunConfigEditorForm extends DBProgramRunConfigurationEditorForm<DBStatementJdwpRunConfig>{
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel hintPanel;
    private JPanel configPanel;
    private JCheckBox isCloudDatabaseCheckBox;

    private final DBJdwpDebugAttributesForm configForm = new DBJdwpDebugAttributesForm(this);

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

        configPanel.add(configForm.getMainPanel());
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void writeConfiguration(DBStatementJdwpRunConfig configuration) throws ConfigurationException {
        configForm.writeConfiguration(configuration);
    }

    @Override
    public void readConfiguration(DBStatementJdwpRunConfig configuration) {
        isCloudDatabaseCheckBox.setSelected(configuration.isTcpDriverTunneling());
        isCloudDatabaseCheckBox.setEnabled(false);
        configForm.readConfiguration(configuration);
    }
}
