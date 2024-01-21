package com.dbn.debugger.jdbc.state;

import com.dbn.debugger.common.state.DBStatementRunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;


public class DBJdbcStatementRunProfileState extends DBStatementRunProfileState {
    public DBJdbcStatementRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }
}
