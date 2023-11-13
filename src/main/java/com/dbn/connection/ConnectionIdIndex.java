package com.dbn.connection;

import java.util.concurrent.atomic.AtomicInteger;

final class ConnectionIdIndex {
    private static final AtomicInteger INDEXER = new AtomicInteger();

    private ConnectionIdIndex() {}

    public static int next() {
        return INDEXER.getAndIncrement();
    }
}
