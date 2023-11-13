package com.dbn.connection.ssh;

import lombok.Value;

@Value
public class SshTunnelConfig {
    private final String proxyHost;
    private final int proxyPort;
    private final String proxyUser;
    private final String proxyPassword;
    private final SshAuthType authType;
    private final String keyFile;
    private final String keyPassphrase;

    private final String remoteHost;
    private final int remotePort;


    public SshTunnelConfig(String proxyHost, int proxyPort, String proxyUser, SshAuthType authType, String keyFile, String keyPassphrase, String proxyPassword, String remoteHost, int remotePort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPassword = proxyPassword;

        this.authType = authType;
        this.keyFile = keyFile;
        this.keyPassphrase = keyPassphrase;

        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }
}
