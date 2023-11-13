package com.dbn.debugger.jdwp.config;

import com.dbn.debugger.common.config.DBRunConfigEditor;
import com.dbn.debugger.jdwp.config.ui.DBStatementJdwpRunConfigEditorForm;
import com.dbn.execution.statement.StatementExecutionInput;

public class DBStatementJdwpRunConfigEditor extends DBRunConfigEditor<DBStatementJdwpRunConfig, DBStatementJdwpRunConfigEditorForm, StatementExecutionInput> {
    public DBStatementJdwpRunConfigEditor(DBStatementJdwpRunConfig configuration) {
        super(configuration);
    }

    @Override
    protected DBStatementJdwpRunConfigEditorForm createConfigurationEditorForm() {
        return new DBStatementJdwpRunConfigEditorForm(getConfiguration());
    }


    @Override
    public void setExecutionInput(StatementExecutionInput executionInput) {
    }
}
