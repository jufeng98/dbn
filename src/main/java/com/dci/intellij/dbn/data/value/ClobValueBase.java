package com.dci.intellij.dbn.data.value;

import com.dci.intellij.dbn.common.exception.Exceptions;
import com.dci.intellij.dbn.common.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;

import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
abstract class ClobValueBase<T extends Clob> extends LargeObjectValue {
    private T clob;
    private Reader reader;

    public ClobValueBase() {
    }

    public ClobValueBase(CallableStatement callableStatement, int parameterIndex) throws SQLException {
        try {
            clob = read(callableStatement, parameterIndex);
        } catch (SQLFeatureNotSupportedException e) {
            conditionallyLog(e);
            String charset = callableStatement.getString(parameterIndex);
            clob = createSerialClob(charset);
        }
    }

    public ClobValueBase(ResultSet resultSet, int columnIndex) throws SQLException {
        try {
            clob = read(resultSet, columnIndex);
        } catch (SQLFeatureNotSupportedException e) {
            conditionallyLog(e);
            String charset = resultSet.getString(columnIndex);
            clob = createSerialClob(charset);
        }
    }

    protected abstract T createClob(Connection connection) throws SQLException;
    protected abstract T createSerialClob(String charset) throws SQLException ;
    protected abstract T read(CallableStatement callableStatement, int parameterIndex) throws SQLException;
    protected abstract T read(ResultSet resultSet, int columnIndex) throws SQLException;
    protected abstract void write(PreparedStatement preparedStatement, int parameterIndex, T clob) throws SQLException;
    protected abstract void write(ResultSet resultSet, int columnIndex, T clob) throws SQLException;

    @Override
    public final void write(Connection connection, PreparedStatement preparedStatement, int parameterIndex, @Nullable String value) throws SQLException {
        try {
            if (Strings.isEmpty(value)) {
                clob = null;
            } else {
                clob = createClob(connection);
                clob.setString(1, value);
            }
            write(preparedStatement, parameterIndex, clob);
        } catch (SQLFeatureNotSupportedException e) {
            conditionallyLog(e);
            if (value == null) {
                preparedStatement.setString(parameterIndex, null);
            } else {
                clob = createSerialClob(value);
                preparedStatement.setString(parameterIndex, value);
            }
        }
    }

    @Override
    public final void write(Connection connection, ResultSet resultSet, int columnIndex, @Nullable String value) throws SQLException {
        try {
            if (Strings.isEmpty(value)) {
                clob = null;
            } else {
                clob = createClob(connection);
                clob.setString(1, value);
            }

            write(resultSet, columnIndex, clob);
        } catch (SQLFeatureNotSupportedException e) {
            conditionallyLog(e);
            if (Strings.isEmpty(value)) {
                clob = null;
            } else {
                clob = createSerialClob(value);
            }
            resultSet.updateString(columnIndex, value);

        }
  }

    @Override
    @Nullable
    public final String read() throws SQLException {
        return read(0);
    }

    @Nullable
    @Override
    public final String export() throws SQLException {
        return read();
    }

    @Override
    public final String read(int maxSize) throws SQLException {
        if (clob == null) return null;

        long totalLength = clob.length();
        int size = (int) (maxSize == 0 ? totalLength : Math.min(maxSize, totalLength));
        try {
            char[] buffer = new char[size];
            reader = clob.getCharacterStream();
            reader.read(buffer, 0, size);
            return new String(buffer);
        } catch (Throwable e) {
            conditionallyLog(e);
            throw Exceptions.toSqlException(e, "Could not read value from CLOB.");
        } finally {
            if (totalLength <= size) release();
        }
    }

    @Override
    public final void release(){
        if (reader == null) return;

        try {
            reader.close();
            reader = null;
        } catch (IOException e) {
            conditionallyLog(e);
            log.error("Could not close CLOB input reader.", e);
        }
    }

    @Override
    public long size() throws SQLException {
        return clob == null ? 0 : clob.length();
    }
}
