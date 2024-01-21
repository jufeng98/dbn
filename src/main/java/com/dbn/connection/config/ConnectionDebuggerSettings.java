package com.dbn.connection.config;

import com.dbn.common.option.InteractiveOptionBroker;
import com.dbn.common.options.BasicConfiguration;
import com.dbn.connection.config.ui.ConnectionDebuggerSettingsForm;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.options.DebuggerTypeOption;
import com.intellij.util.Range;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.options.setting.Settings.*;

@Getter
@Setter
public class ConnectionDebuggerSettings extends BasicConfiguration<ConnectionSettings, ConnectionDebuggerSettingsForm> {
    private boolean compileDependencies = true;
    private boolean tcpDriverTunneling;
    private String tcpHostAddress;
    private Range<Integer> tcpPortRange = new Range<>(4000, 4999);

    private final InteractiveOptionBroker<DebuggerTypeOption> debuggerType =
            new InteractiveOptionBroker<>(
                    "debugger-type",
                    "Debugger Type",
                    "Please select debugger type to use.",
                    DBDebuggerType.JDWP.isSupported() ? DebuggerTypeOption.ASK : DebuggerTypeOption.JDBC,
                    DebuggerTypeOption.JDWP,
                    DebuggerTypeOption.JDBC,
                    DebuggerTypeOption.CANCEL);

    public ConnectionDebuggerSettings() {
        super(null);
    }

    public ConnectionDebuggerSettings(ConnectionSettings parent) {
        super(parent);
    }

    @Override
    @NotNull
    public ConnectionDebuggerSettingsForm createConfigurationEditor() {
        return new ConnectionDebuggerSettingsForm(this);
    }

    public String getConfigElementName() {
        return "debugger";
    }

    @Override
    public void readConfiguration(Element element) {
        compileDependencies = getBoolean(element, "compile-dependencies", compileDependencies);
        tcpDriverTunneling = getBoolean(element, "tcp-driver-tunneling", tcpDriverTunneling);
        tcpHostAddress = getString(element, "tcp-host-address", tcpHostAddress);
        int tcpPortFrom = getInteger(element, "tcp-port-from", tcpPortRange.getFrom());
        int tcpPortTo = getInteger(element, "tcp-port-to", tcpPortRange.getTo());
        tcpPortRange = new Range<>(tcpPortFrom, tcpPortTo);
        debuggerType.readConfiguration(element);
    }

    @Override
    public void writeConfiguration(Element element) {
        setBoolean(element, "compile-dependencies", compileDependencies);
        setBoolean(element, "tcp-driver-tunneling", tcpDriverTunneling);
        setString(element, "tcp-host-address", tcpHostAddress);
        setInteger(element, "tcp-port-from", tcpPortRange.getFrom());
        setInteger(element, "tcp-port-to", tcpPortRange.getTo());
        debuggerType.writeConfiguration(element);
    }
}
