package com.dbn.debugger.jdbc.config;

import com.dbn.debugger.jdbc.config.ui.DBMethodJdbcRunConfigEditorForm;
import com.dbn.debugger.common.config.DBRunConfigEditor;
import com.dbn.execution.method.MethodExecutionInput;

public class DBMethodJdbcRunConfigEditor extends DBRunConfigEditor<DBMethodJdbcRunConfig, DBMethodJdbcRunConfigEditorForm, MethodExecutionInput> {
    public DBMethodJdbcRunConfigEditor(DBMethodJdbcRunConfig configuration) {
        super(configuration);
    }

    @Override
    protected DBMethodJdbcRunConfigEditorForm createConfigurationEditorForm() {
        return new DBMethodJdbcRunConfigEditorForm(getConfiguration());
    }


    @Override
    public void setExecutionInput(MethodExecutionInput executionInput) {
        DBMethodJdbcRunConfigEditorForm configurationEditorForm = getConfigurationEditorForm(false);
        if (configurationEditorForm != null) {
            configurationEditorForm.setExecutionInput(executionInput, true);
        }
    }
}
