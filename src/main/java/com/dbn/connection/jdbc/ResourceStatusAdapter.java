package com.dbn.connection.jdbc;

import java.sql.SQLException;

public interface ResourceStatusAdapter<T extends Resource> {
    boolean get();

    void set(boolean value) throws SQLException;
}
