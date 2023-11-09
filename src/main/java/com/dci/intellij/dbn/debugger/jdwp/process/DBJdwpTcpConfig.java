package com.dci.intellij.dbn.debugger.jdwp.process;

import lombok.Getter;

@Getter
public class DBJdwpTcpConfig {
    private final String host;
    private final int port;
    private final boolean tunneled;

    public DBJdwpTcpConfig(String host, int port) {
        this(host, port, false);
    }

    public DBJdwpTcpConfig(String host, int port, boolean tunneled) {
        this.host = host;
        this.port = port;
        this.tunneled = tunneled;
    }

    public boolean isLocal() {
        return !isTunneled();
    }
}
