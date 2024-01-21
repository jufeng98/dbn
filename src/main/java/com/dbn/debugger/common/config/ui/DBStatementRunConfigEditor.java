package com.dbn.debugger.common.config.ui;

import com.dbn.debugger.common.config.DBRunConfigEditor;
import com.dbn.debugger.common.config.DBStatementRunConfig;
import com.dbn.execution.statement.StatementExecutionInput;

public class DBStatementRunConfigEditor extends DBRunConfigEditor<DBStatementRunConfig, DBStatementRunConfigForm, StatementExecutionInput> {
    public DBStatementRunConfigEditor(DBStatementRunConfig configuration) {
        super(configuration);
    }

    @Override
    protected DBStatementRunConfigForm createConfigurationEditorForm() {
        return new DBStatementRunConfigForm(getConfiguration());
    }

    @Override
    public void setExecutionInput(StatementExecutionInput executionInput) {

    }
}
