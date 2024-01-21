package com.dbn.debugger.jdbc.state;

import com.dbn.debugger.common.state.DBMethodRunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;


public class DBJdbcMethodRunProfileState extends DBMethodRunProfileState {
    public DBJdbcMethodRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }
}
