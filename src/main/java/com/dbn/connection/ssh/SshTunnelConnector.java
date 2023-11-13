package com.dbn.connection.ssh;

import com.dbn.common.util.Commons;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Getter
public class SshTunnelConnector {
    private final SshTunnelConfig config;

    private final String localHost = "localhost";
    private int localPort;
    private Session session;

    public SshTunnelConnector(SshTunnelConfig config) {
        this.config = config;
    }

    public Session connect() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            localPort = serverSocket.getLocalPort();
        }
        catch (IOException e) {
            conditionallyLog(e);
            throw new JSchException("Can't find a free port", e);
        }

        JSch jsch = new JSch();
/*
        TODO open ssl config file
        ConfigRepository configRepository = OpenSSHConfig.parse("");
        jsch.setConfigRepository(configRepository);
*/

        JSch.setConfig("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
        session = jsch.getSession(
                config.getProxyUser(),
                config.getProxyHost(),
                config.getProxyPort());

        if(config.getAuthType() == SshAuthType.KEY_PAIR) {
            String keyFile = config.getKeyFile();
            String keyPassphrase = Commons.nvl(config.getKeyPassphrase(), "");
            jsch.addIdentity(keyFile, keyPassphrase);;
        } else {
            session.setPassword(config.getProxyPassword());
        }

        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        properties.put("TCPKeepAlive", "yes");
        session.setConfig(properties);
        session.setServerAliveInterval((int) TimeUnit.MINUTES.toMillis(2L));
        session.setServerAliveCountMax(1000);
        session.connect();

        session.setPortForwardingL(localPort, config.getRemoteHost(), config.getRemotePort());
        return session;
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }
}
