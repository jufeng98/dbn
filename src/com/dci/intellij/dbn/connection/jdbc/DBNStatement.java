package com.dci.intellij.dbn.connection.jdbc;

import java.lang.ref.WeakReference;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.dci.intellij.dbn.common.dispose.FailsafeUtil;

public class DBNStatement<T extends Statement> extends DBNResource implements Statement, Closeable, Cancellable {
    protected T inner;
    protected SQLException exception;

    private WeakReference<DBNConnection> connection;
    private WeakReference<DBNResultSet> resultSet;


    DBNStatement(T inner, DBNConnection connection) {
        this.inner = inner;
        this.connection = new WeakReference<>(connection);
    }

    @Override
    public DBNConnection getConnection() {
        return FailsafeUtil.get(connection.get());
    }


    @Override
    public boolean isCancelledInner() throws SQLException {
        return false;
    }

    @Override
    public void cancelInner() throws SQLException {
        inner.cancel();
    }

    @Override
    public boolean isClosedInner() throws SQLException {
        return inner.isClosed();
    }

    @Override
    public void closeInner() throws SQLException {
        inner.close();
    }

    @Override
    public void close() {
        try {
            super.close();
        } finally {
            DBNConnection connection = this.connection.get();
            if (connection != null) {
                connection.release(this);
            }
        }
    }

    protected DBNResultSet wrap(ResultSet original) {
        if (original == null) {
            resultSet = null;
        } else {
            if (resultSet == null) {
                resultSet = new WeakReference<>(new DBNResultSet(original, this));
            } else {
                DBNResultSet wrapped = resultSet.get();
                if (wrapped == null || wrapped.inner != original) {
                    resultSet = new WeakReference<>(new DBNResultSet(original, this));
                }
            }
        }
        return this.resultSet == null ? null : this.resultSet.get();
    }

    protected Object wrap(Object object) {
        if (object instanceof ResultSet) {
            ResultSet resultSet = (ResultSet) object;
            return new DBNResultSet(resultSet, getConnection());
        }
        return object;
    }

    /********************************************************************
     *                     Wrapped executions                           *
     ********************************************************************/
    @Override
    public boolean execute(String sql) throws SQLException {
        try {
            return inner.execute(sql);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public DBNResultSet executeQuery(String sql) throws SQLException {
        try {
            return wrap(inner.executeQuery(sql));
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        try {
            return inner.executeUpdate(sql);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return inner.executeUpdate(sql, autoGeneratedKeys);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        try {
            return inner.executeUpdate(sql, columnIndexes);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        try {
            return inner.executeUpdate(sql, columnNames);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        try {
            return inner.execute(sql, autoGeneratedKeys);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        try {
            return inner.execute(sql, columnIndexes);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        try {
            return inner.execute(sql, columnNames);
        } catch (SQLException e){
            throw this.exception = e;
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        try {
            return inner.executeBatch();
        } catch (SQLException e){
            throw this.exception = e;
        }
    }


    /********************************************************************
     *                     Wrapped functionality                        *
     ********************************************************************/
    @Override
    public int getMaxFieldSize() throws SQLException {
        return inner.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        inner.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return inner.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        inner.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        inner.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return inner.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        inner.setQueryTimeout(seconds);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return inner.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        inner.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        inner.setCursorName(name);
    }

    @Override
    public DBNResultSet getResultSet() throws SQLException {
        return wrap(inner.getResultSet());
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return inner.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return inner.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        inner.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return inner.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        inner.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return inner.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return inner.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return inner.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        inner.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        inner.clearBatch();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return inner.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return inner.getGeneratedKeys();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return inner.getResultSetHoldability();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        inner.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return inner.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        inner.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return inner.isCloseOnCompletion();
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return inner.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return inner.isWrapperFor(iface);
    }
}
