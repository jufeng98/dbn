package com.dbn.debugger.jdwp.config;

import com.dbn.debugger.common.config.DBRunConfigEditor;
import com.dbn.debugger.jdwp.config.ui.DBMethodJdwpRunConfigEditorForm;
import com.dbn.execution.method.MethodExecutionInput;

public class DBMethodJdwpRunConfigEditor extends DBRunConfigEditor<DBMethodJdwpRunConfig, DBMethodJdwpRunConfigEditorForm, MethodExecutionInput> {
    public DBMethodJdwpRunConfigEditor(DBMethodJdwpRunConfig configuration) {
        super(configuration);
    }

    @Override
    protected DBMethodJdwpRunConfigEditorForm createConfigurationEditorForm() {
        return new DBMethodJdwpRunConfigEditorForm(getConfiguration());
    }


    @Override
    public void setExecutionInput(MethodExecutionInput executionInput) {
        DBMethodJdwpRunConfigEditorForm configurationEditorForm = getConfigurationEditorForm(false);
        if (configurationEditorForm != null) {
            configurationEditorForm.setExecutionInput(executionInput, true);
        }
    }
}
