package com.dbn.execution.script.options;

import com.dbn.common.options.BasicProjectConfiguration;
import com.dbn.common.options.setting.Settings;
import com.dbn.common.project.ProjectSupplier;
import com.dbn.execution.script.options.ui.ScriptExecutionSettingsForm;
import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.execution.common.options.ExecutionTimeoutSettings;
import com.dbn.execution.script.CmdLineInterface;
import com.dbn.execution.script.CmdLineInterfaceBundle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.options.setting.Settings.newElement;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ScriptExecutionSettings extends BasicProjectConfiguration<ExecutionEngineSettings, ScriptExecutionSettingsForm> implements ExecutionTimeoutSettings, ProjectSupplier {
    private CmdLineInterfaceBundle commandLineInterfaces = new CmdLineInterfaceBundle();
    private int executionTimeout = 300;

    public ScriptExecutionSettings(ExecutionEngineSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public ScriptExecutionSettingsForm createConfigurationEditor() {
        return new ScriptExecutionSettingsForm(this);
    }

    @NotNull
    public CmdLineInterface getCommandLineInterface(String id) {
        return commandLineInterfaces.getInterface(id);
    }

    @Override
    public int getDebugExecutionTimeout() {
        return 0;
    }

    @Override
    public void setDebugExecutionTimeout(int timeout) {}


    @Override
    public String getConfigElementName() {
        return "script-execution";
    }

    @Override
    public void readConfiguration(Element element) {
        Element executorsElement = element.getChild("command-line-interfaces");
        commandLineInterfaces.readConfiguration(executorsElement);
        executionTimeout = Settings.getInteger(element, "execution-timeout", executionTimeout);
    }

    @Override
    public void writeConfiguration(Element element) {
        Element executorsElement = newElement(element, "command-line-interfaces");
        commandLineInterfaces.writeConfiguration(executorsElement);
        Settings.setInteger(element, "execution-timeout", executionTimeout);
    }
}
