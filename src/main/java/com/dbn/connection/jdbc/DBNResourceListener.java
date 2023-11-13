package com.dbn.connection.jdbc;

import java.util.EventListener;

public interface DBNResourceListener extends EventListener {
    default void closing() {}

    default void closed() {}
}
