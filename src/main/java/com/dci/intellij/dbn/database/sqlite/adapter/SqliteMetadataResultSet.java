package com.dci.intellij.dbn.database.sqlite.adapter;

import com.dci.intellij.dbn.common.cache.Cache;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.common.util.ResultSetStub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Deprecated // replace with com.dci.intellij.dbn.database.common.util.CachedResultSet
public class SqliteMetadataResultSet<T extends SqliteMetadataResultSetRow>
        extends StatefulDisposableBase
        implements ResultSetStub {

    private final List<T> rows = new ArrayList<>();
    private int cursor = -1;

    @Override
    public boolean next() throws SQLException {
        cursor++;
        return cursor < rows.size();
    }

    protected T current() {
        return rows.get(cursor);
    }

    public void add(T element) {
        rows.add(element);
        rows.sort(null);
    }

    protected T row(String name) {
        for (T element : rows) {
            if (Strings.equalsIgnoreCase(element.identifier(), name)) {
                return element;
            }
        }
        return null;
    }

    protected static String toFlag(boolean value) {
        return value ? "Y" : "N";
    }

    @Override
    public void close() throws SQLException {
        // nothing to close
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    protected static Cache cache() {
        return ConnectionHandler.local().getMetaDataCache();
    }

    @Override
    public void disposeInner() {
        nullify();
    }
}
