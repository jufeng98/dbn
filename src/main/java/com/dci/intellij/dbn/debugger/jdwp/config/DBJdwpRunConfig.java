package com.dci.intellij.dbn.debugger.jdwp.config;

import com.dci.intellij.dbn.connection.config.ConnectionDebuggerSettings;
import com.intellij.util.Range;

/**
 * // TODO DBN-586 (transient run configs) - relocated debug settings
 * @deprecated moved to {@link ConnectionDebuggerSettings}
 */

public interface DBJdwpRunConfig {

    /**
     * @deprecated moved to {@link ConnectionDebuggerSettings#getTcpPortRange()}
     */
    Range<Integer> getTcpPortRange();

    /**
     * @deprecated moved to {@link ConnectionDebuggerSettings#getTcpHostAddress()}
     */
    String getTcpHostAddress();

    /**
     * @deprecated moved to {@link ConnectionDebuggerSettings#isCompileDependencies()}
     */
    boolean isCompileDependencies();

    void setTcpPortRange(Range<Integer> portRange);

    void setTcpHostAddress(String tcpHost);

    void setCompileDependencies(boolean selected);

    default boolean getIsCloudDatabase(){return false;}
}
