package com.dci.intellij.dbn.connection.jdbc;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.routine.ThrowableCallable;
import com.dci.intellij.dbn.common.util.Unsafe;
import com.dci.intellij.dbn.connection.Resources;
import com.dci.intellij.dbn.diagnostics.Diagnostics;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.dci.intellij.dbn.connection.Resources.markClosed;
import static com.dci.intellij.dbn.connection.jdbc.ResourceStatus.ACTIVE;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@Getter
@Setter
public class DBNStatement<T extends Statement> extends DBNResource<T> implements Statement, CloseableResource, CancellableResource {
    private final WeakRef<DBNConnection> connection;
    private WeakRef<DBNResultSet> resultSet;

    /** last execution time. -1 unknown */
    private final AtomicLong executeDuration = new AtomicLong(-1);

    private boolean cached;
    private String sql;

    DBNStatement(T inner, DBNConnection connection) {
        super(inner, ResourceType.STATEMENT);
        this.connection = WeakRef.of(connection);
    }

    @Override
    @Nullable
    public DBNConnection getConnection() {
        return connection.get();
    }

    @NotNull
    public DBNConnection ensureConnection() {
        return Failsafe.nn(getConnection());
    }

    @Override
    public boolean isObsolete() {
        if (isClosed()) return true;

        DBNConnection connection = getConnection();
        if (connection == null) return true;
        if (connection.isClosed()) return true;

        return false;
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
    public void close() throws SQLException {
        try {
            super.close();
        } finally {
            DBNConnection connection = getConnection();
            if (connection != null) {
                connection.release(this);
            }
        }
    }

    public void park() {
        DBNConnection connection = getConnection();
        if (connection != null) {
            connection.park(this);
        }
    }

    protected DBNResultSet wrap(ResultSet original) throws SQLException {
        if (original == null) {
            resultSet = null;
        } else {
            if (resultSet == null) {
                DBNResultSet resultSet = new DBNResultSet(original, this);
                this.resultSet = WeakRef.of(resultSet);
            } else {
                DBNResultSet resultSet = this.resultSet.get();
                if (resultSet == null || resultSet.inner != original) {
                    resultSet = new DBNResultSet(original, this);
                    this.resultSet = WeakRef.of(resultSet);

                }
            }
        }
        return WeakRef.get(resultSet);
    }

    public long getExecuteDuration() {
        return executeDuration.get();
    }

    protected Object wrap(Object object) {
        if (object instanceof ResultSet) {
            ResultSet resultSet = (ResultSet) object;
            return new DBNResultSet(resultSet, ensureConnection());
        }
        return object;
    }

    protected <R> R managed(ThrowableCallable<R, SQLException> callable) throws SQLException {
        DBNConnection connection = ensureConnection();
        connection.updateLastAccess();
        try {
            connection.set(ACTIVE, true);
            executeDuration.set(-1);
            long init = System.currentTimeMillis();
            try {
                return callable.call();
            } finally {
                executeDuration.set(System.currentTimeMillis() - init);
            }
        } catch (SQLRecoverableException e) {
            conditionallyLog(e);
            markClosed(connection);
            throw e;
        } catch (SQLException e) {
            conditionallyLog(e);
            Resources.close(DBNStatement.this);
            connection.reevaluateStatus();
            throw e;
        } finally {
            connection.updateLastAccess();
            connection.set(ACTIVE, false);
        }
    }

    /********************************************************************
     *                     Wrapped executions                           *
     ********************************************************************/
    @Override
    public boolean execute(String sql) throws SQLException {
        return managed(() -> inner.execute(sql));
    }

    @Override
    public DBNResultSet executeQuery(String sql) throws SQLException {
        return managed(() -> wrap(inner.executeQuery(sql)));
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return managed(() -> inner.executeUpdate(sql));
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return managed(() -> inner.executeUpdate(sql, autoGeneratedKeys));
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return managed(() -> inner.executeUpdate(sql, columnIndexes));
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return managed(() -> inner.executeUpdate(sql, columnNames));
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return managed(() -> inner.execute(sql, autoGeneratedKeys));
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return managed(() -> inner.execute(sql, columnIndexes));
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return managed(() -> inner.execute(sql, columnNames));
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return managed(() -> inner.executeBatch());
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
        try {
            seconds = Diagnostics.timeoutAdjustment(seconds);
            inner.setQueryTimeout(seconds);
        } catch (Throwable e) {
            conditionallyLog(e);
            // catch throwable (capture e.g. java.lang.AbstractMethodError)
            // not all databases support it, as this is used on DBN start connection, we must control exception
        }
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
    public void setFetchSize(int rows) {
        Unsafe.silent(this, s -> s.inner.setFetchSize(Math.max(rows, 100)));
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
