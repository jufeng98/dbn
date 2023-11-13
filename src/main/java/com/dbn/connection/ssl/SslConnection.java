package com.dbn.connection.ssl;

import lombok.Getter;

@Getter
public class SslConnection {
    private final SslConnectionConfig config;

    public SslConnection(SslConnectionConfig config) {
        this.config = config;
    }

    public boolean isConnected() {
        // TODO
        return false;
    }

    public void connect() {
        // TODO
    }
}
