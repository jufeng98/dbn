package com.dbn.connection.jdbc;

import java.sql.SQLException;

public interface CloseableResource extends AutoCloseable, Resource{
    boolean isClosed() throws SQLException;

    boolean isClosedInner() throws SQLException;

    @Override
    void close() throws SQLException;

    void closeInner() throws SQLException;
}
