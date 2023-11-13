package com.dbn.connection;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.concurrent.Callable;

@UtilityClass
public class ConnectionIdContext {
    private static final ThreadLocal<ConnectionId> CONNECTION_ID = new ThreadLocal<>();

    public static ConnectionId get() {
        return CONNECTION_ID.get();
    }

    @SneakyThrows
    public static <T> T surround(ConnectionId connectionId, Callable<T> callable) {
        boolean initialized = init(connectionId);
        try {
            return callable.call();
        } finally {
            release(initialized);
        }
    }

     @SneakyThrows
     public static void surround(ConnectionId connectionId, Runnable runnable) {
        boolean initialized = init(connectionId);
        try {
            runnable.run();
        } finally {
            release(initialized);
        }
    }

    private static boolean init(ConnectionId connectionId) {
        ConnectionId localConnectionId = CONNECTION_ID.get();
        if (localConnectionId == null) {
            CONNECTION_ID.set(connectionId);
            return true;
        }

        if (!Objects.equals(connectionId, localConnectionId)) {
            throw new IllegalStateException("Context already initialized for another connection");
        }
        return false;
    }

    private static void release(boolean initialized) {
        if (!initialized) return;
        CONNECTION_ID.remove();
    }
}
