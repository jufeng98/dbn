package com.dci.intellij.dbn.data.value;

import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.data.type.GenericDataType;

import javax.sql.rowset.serial.SerialClob;
import java.sql.*;

public class ClobValue extends ClobValueBase<Clob>{

    public ClobValue() {
    }

    public ClobValue(CallableStatement callableStatement, int parameterIndex) throws SQLException {
        super(callableStatement, parameterIndex);
    }

    public ClobValue(ResultSet resultSet, int columnIndex) throws SQLException {
        super(resultSet, columnIndex);
    }

    @Override
    protected Clob createClob(Connection connection) throws SQLException {
        return connection.createClob();
    }

    @Override
    protected Clob createSerialClob(String charset) throws SQLException {
        if (Strings.isEmpty(charset)) return null;
        SerialClob serialClob = new SerialClob(charset.toCharArray());
        return serialClob;
    }

    @Override
    protected Clob read(CallableStatement callableStatement, int parameterIndex) throws SQLException {
        return callableStatement.getClob(parameterIndex);
    }

    @Override
    protected Clob read(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getClob(columnIndex);
    }

    @Override
    protected void write(PreparedStatement preparedStatement, int parameterIndex, Clob clob) throws SQLException {
        preparedStatement.setClob(parameterIndex, clob);
    }

    @Override
    protected void write(ResultSet resultSet, int columnIndex, Clob clob) throws SQLException {
        resultSet.updateClob(columnIndex, clob);
    }

    @Override
    public GenericDataType getGenericDataType() {
        return GenericDataType.CLOB;
    }

    @Override
    public String getDisplayValue() {
        return "[CLOB]";
    }
}
