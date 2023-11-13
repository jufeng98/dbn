package com.dbn.connection.config.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.connection.config.ConnectionDebuggerSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.Range;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ConnectionDebuggerSettingsForm extends ConfigurationEditorForm<ConnectionDebuggerSettings> {
    private JPanel mainPanel;
    private JCheckBox compileDependenciesCheckBox;
    private JCheckBox tcpDriverTunnelingCheckBox;
    private JTextField tcpHostTextBox;
    private JTextField tcpPortFromTextField;
    private JTextField tcpPortToTextField;

    public ConnectionDebuggerSettingsForm(ConnectionDebuggerSettings configuration) {
        super(configuration);

        resetFormChanges();
        updateTcpFields();
        registerComponent(mainPanel);
    }

    @Override
    protected JComponent getMainComponent() {
        return mainPanel;
    }

    protected ActionListener createActionListener() {
        return e -> {
            Object source = e.getSource();
            if (source == tcpDriverTunnelingCheckBox) updateTcpFields();

            getConfiguration().setModified(true);
        };
    }

    private void updateTcpFields() {
        boolean tunneling = tcpDriverTunnelingCheckBox.isSelected();
        tcpHostTextBox.setEnabled(!tunneling);
        tcpPortFromTextField.setEnabled(!tunneling);
        tcpPortToTextField.setEnabled(!tunneling);
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        ConnectionDebuggerSettings configuration = getConfiguration();
        applyFormChanges(configuration);
    }


    @Override
    public void applyFormChanges(ConnectionDebuggerSettings configuration) throws ConfigurationException {
        configuration.setCompileDependencies(compileDependenciesCheckBox.isSelected());
        configuration.setTcpDriverTunneling(tcpDriverTunnelingCheckBox.isSelected());
        configuration.setTcpHostAddress(tcpHostTextBox.getText());

        try {
            configuration.setTcpPortRange(new Range<>(
                    Integer.parseInt(tcpPortFromTextField.getText()),
                    Integer.parseInt(tcpPortToTextField.getText())));
        } catch (NumberFormatException e) {
            throw new ConfigurationException("TCP Port Range inputs must me numeric");
        }
    }

    @Override
    public void resetFormChanges() {
        ConnectionDebuggerSettings configuration = getConfiguration();
        compileDependenciesCheckBox.setSelected(configuration.isCompileDependencies());
        tcpDriverTunnelingCheckBox.setSelected(configuration.isTcpDriverTunneling());
        tcpHostTextBox.setText(configuration.getTcpHostAddress());
        tcpPortFromTextField.setText(String.valueOf(configuration.getTcpPortRange().getFrom()));
        tcpPortToTextField.setText(String.valueOf(configuration.getTcpPortRange().getTo()));
        updateTcpFields();
    }
}
