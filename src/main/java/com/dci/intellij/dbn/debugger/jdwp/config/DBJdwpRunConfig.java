package com.dci.intellij.dbn.debugger.jdwp.config;

import com.intellij.util.Range;
import java.net.InetAddress;

public interface DBJdwpRunConfig {
    Range<Integer> getTcpPortRange();

    String getTcpHostAddress();

    boolean isCompileDependencies();

    void setTcpPortRange(Range<Integer> integerRange);

    void setTcpHostAddress(String tcpHost);

    void setCompileDependencies(boolean selected);
}
