package com.dbn.debugger.common.config.ui;

import com.dbn.debugger.common.config.DBMethodRunConfig;
import com.dbn.debugger.common.config.DBRunConfigEditor;
import com.dbn.execution.method.MethodExecutionInput;

public class DBMethodRunConfigEditor extends DBRunConfigEditor<DBMethodRunConfig, DBMethodRunConfigForm, MethodExecutionInput> {
    public DBMethodRunConfigEditor(DBMethodRunConfig configuration) {
        super(configuration);
    }

    @Override
    protected DBMethodRunConfigForm createConfigurationEditorForm() {
        return new DBMethodRunConfigForm(getConfiguration());
    }


    @Override
    public void setExecutionInput(MethodExecutionInput executionInput) {
        DBMethodRunConfigForm configurationEditorForm = getConfigurationEditorForm(false);
        if (configurationEditorForm != null) {
            configurationEditorForm.setExecutionInput(executionInput, true);
        }
    }
}
