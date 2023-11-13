package com.dbn.database.common.debug;

import com.dbn.database.common.statement.CallableStatementOutput;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;


public class DebuggerVersionInfo implements CallableStatementOutput {
    private String version;


    public String getVersion() {
        return version;
    }

    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.VARCHAR);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        version = statement.getString(1);
    }
}
