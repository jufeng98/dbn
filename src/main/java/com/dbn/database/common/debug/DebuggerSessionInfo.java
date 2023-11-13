package com.dbn.database.common.debug;

import com.dbn.database.common.statement.CallableStatementOutput;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;


public class DebuggerSessionInfo implements CallableStatementOutput {
    private String sessionId;


    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.VARCHAR);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        sessionId = statement.getString(1);
    }
}
