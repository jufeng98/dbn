package com.dbn.connection.ssl;

import lombok.Value;

import java.io.File;

@Value
public class SslConnectionConfig {
    private final File certificateAuthorityFile;
    private final File clientCertificateFile;
    private final File clientKeyFile;

    public SslConnectionConfig(File certificateAuthorityFile, File clientCertificateFile, File clientKeyFile) {
        this.certificateAuthorityFile = certificateAuthorityFile;
        this.clientCertificateFile = clientCertificateFile;
        this.clientKeyFile = clientKeyFile;
    }
}
