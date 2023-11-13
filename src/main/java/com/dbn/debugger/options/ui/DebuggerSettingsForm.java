package com.dbn.debugger.options.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.debugger.options.DebuggerSettings;
import com.dbn.debugger.options.DebuggerTypeOption;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dbn.common.ui.util.ComboBoxes.*;

public class DebuggerSettingsForm extends ConfigurationEditorForm<DebuggerSettings> {
    private JPanel mainPanel;
    private JComboBox<DebuggerTypeOption> debuggerTypeComboBox;

    public DebuggerSettingsForm(DebuggerSettings settings) {
        super(settings);

        initComboBox(debuggerTypeComboBox,
                DebuggerTypeOption.JDWP,
                DebuggerTypeOption.JDBC,
                DebuggerTypeOption.ASK);
        resetFormChanges();
        registerComponent(mainPanel);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        DebuggerSettings settings = getConfiguration();
        settings.getDebuggerType().set(getSelection(debuggerTypeComboBox));
    }

    @Override
    public void resetFormChanges() {
        DebuggerSettings settings = getConfiguration();
        setSelection(debuggerTypeComboBox, settings.getDebuggerType().get());
    }
}
