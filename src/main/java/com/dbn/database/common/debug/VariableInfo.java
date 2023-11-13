package com.dbn.database.common.debug;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;      

public class VariableInfo extends BasicOperationInfo {
    private String value;

    @Override
    public void registerParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(1, Types.VARCHAR);
        statement.registerOutParameter(2, Types.VARCHAR);
    }

    @Override
    public void read(CallableStatement statement) throws SQLException {
        value = statement.getString(1);
        error = statement.getString(2);
    }

    public String getValue() {
        return value;
    }

}
