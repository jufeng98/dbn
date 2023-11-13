package com.dbn.debugger.jdbc.config;

import com.dbn.debugger.jdbc.config.ui.DBStatementJdbcRunConfigurationEditorForm;
import com.dbn.debugger.common.config.DBRunConfigEditor;
import com.dbn.execution.statement.StatementExecutionInput;

public class DBStatementJdbcRunConfigEditor extends DBRunConfigEditor<DBStatementJdbcRunConfig, DBStatementJdbcRunConfigurationEditorForm, StatementExecutionInput> {
    public DBStatementJdbcRunConfigEditor(DBStatementJdbcRunConfig configuration) {
        super(configuration);
    }

    @Override
    protected DBStatementJdbcRunConfigurationEditorForm createConfigurationEditorForm() {
        return new DBStatementJdbcRunConfigurationEditorForm(getConfiguration());
    }

    @Override
    public void setExecutionInput(StatementExecutionInput executionInput) {

    }
}
