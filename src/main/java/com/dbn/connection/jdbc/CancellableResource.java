package com.dbn.connection.jdbc;

import java.sql.SQLException;

public interface CancellableResource extends Resource {
    boolean isCancelled() throws SQLException;

    boolean isCancelledInner() throws SQLException;

    void cancel() throws SQLException;

    void cancelInner() throws SQLException;
}
