package com.dbn.debugger.options;

import com.dbn.common.option.InteractiveOptionBroker;
import com.dbn.common.options.BasicConfiguration;
import com.dbn.connection.operation.options.OperationSettings;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.options.ui.DebuggerSettingsForm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DebuggerSettings extends BasicConfiguration<OperationSettings, DebuggerSettingsForm> {
    private final InteractiveOptionBroker<DebuggerTypeOption> debuggerType =
            new InteractiveOptionBroker<>(
                    "debugger-type",
                    "Debugger Type",
                    "Please select debugger type to use.",
                    DBDebuggerType.JDWP.isSupported() ? DebuggerTypeOption.ASK : DebuggerTypeOption.JDBC,
                    DebuggerTypeOption.JDWP,
                    DebuggerTypeOption.JDBC,
                    DebuggerTypeOption.CANCEL);

    public DebuggerSettings(OperationSettings parent) {
        super(parent);
    }

    @Override
    public String getDisplayName() {
        return "Data editor general settings";
    }

    @Override
    public String getHelpTopic() {
        return "debugger";
    }

    /****************************************************
     *                   Configuration                  *
     ****************************************************/
    @Override
    @NotNull
    public DebuggerSettingsForm createConfigurationEditor() {
        return new DebuggerSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "debugger";
    }

    @Override
    public void readConfiguration(Element element) {
        debuggerType.readConfiguration(element);
    }

    @Override
    public void writeConfiguration(Element element) {
        debuggerType.writeConfiguration(element);
    }
}
