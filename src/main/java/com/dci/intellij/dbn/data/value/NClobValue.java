package com.dci.intellij.dbn.data.value;

import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.data.type.GenericDataType;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class NClobValue extends ClobValueBase<NClob> {
    public NClobValue() {
    }

    public NClobValue(CallableStatement callableStatement, int parameterIndex) throws SQLException {
        super(callableStatement, parameterIndex);
    }

    public NClobValue(ResultSet resultSet, int columnIndex) throws SQLException {
        super(resultSet, columnIndex);
    }

    @Override
    protected NClob createClob(Connection connection) throws SQLException {
        return connection.createNClob();
    }

    protected NClob createSerialClob(String charset) throws SQLException {
        if (Strings.isEmpty(charset)) return null;
        return new SerialNClob(charset.toCharArray());
    }

    @Override
    protected NClob read(CallableStatement callableStatement, int parameterIndex) throws SQLException {
        return callableStatement.getNClob(parameterIndex);
    }

    @Override
    protected NClob read(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getNClob(columnIndex);
    }

    @Override
    protected void write(PreparedStatement preparedStatement, int parameterIndex, NClob clob) throws SQLException {
        preparedStatement.setNClob(parameterIndex, clob);
    }

    @Override
    protected void write(ResultSet resultSet, int columnIndex, NClob clob) throws SQLException {
        resultSet.updateNClob(columnIndex, clob);
    }

    @Override
    public GenericDataType getGenericDataType() {
        return GenericDataType.NCLOB;
    }


    @Override
    public String getDisplayValue() {
        return "[NCLOB]";
    }
}
