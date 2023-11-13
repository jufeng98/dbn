package com.dbn.database.oracle;

import com.dbn.common.util.Strings;
import com.dbn.database.interfaces.DatabaseEnvironmentInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class OracleEnvironmentInterface implements DatabaseEnvironmentInterface {
    public static final String CLOUD_DATABASE_PATTERN = ".+\\.ade\\..+\\.oraclecloud\\.com";
    public static final List<String> cloudHostnames = Strings.tokenize(System.getProperty("cloud.hostnames"), ",");


    @Override
    public boolean isCloudDatabase(String hostname) {
        if (Strings.isEmptyOrSpaces(hostname)) return false;
        if (hostname.matches(CLOUD_DATABASE_PATTERN)) return true;
        if (cloudHostnames.contains(hostname)) return true;

        // TODO all false from here, do we need these?
        if (hostname.equals("localhost")) return false;
        if (hostname.equals("127.0.0.1")) return false;
        try {
            if (hostname.equals(InetAddress.getLocalHost().getHostAddress())) return false;
        } catch (UnknownHostException e) {
            return false;
        }

        return false;
    }
}
