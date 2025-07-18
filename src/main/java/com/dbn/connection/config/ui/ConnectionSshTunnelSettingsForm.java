package com.dbn.connection.config.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.options.ui.ConfigurationEditors;
import com.dbn.connection.config.ConnectionSshTunnelSettings;
import com.dbn.connection.ssh.SshAuthType;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;

import static com.dbn.common.ui.util.ComboBoxes.getSelection;
import static com.dbn.common.ui.util.ComboBoxes.initComboBox;
import static com.dbn.common.ui.util.ComboBoxes.setSelection;

public class ConnectionSshTunnelSettingsForm extends ConfigurationEditorForm<ConnectionSshTunnelSettings> {
    private JPanel mainPanel;
    private JPanel sshGroupPanel;
    private JTextField hostTextField;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JTextField portTextField;
    private JCheckBox activeCheckBox;
    private JComboBox<SshAuthType> authTypeComboBox;
    private JPasswordField keyPassphraseField;
    private TextFieldWithBrowseButton keyFileField;
    private JLabel passwordLabel;
    private JLabel privateKeyFileLabel;
    private JLabel privateKeyPassphraseLabel;

    public ConnectionSshTunnelSettingsForm(final ConnectionSshTunnelSettings configuration) {
        super(configuration);

        initComboBox(authTypeComboBox, SshAuthType.values());
        resetFormChanges();

        authTypeComboBox.addActionListener(e -> showHideFields());

        enableDisableFields();
        showHideFields();
        registerComponent(mainPanel);

        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false,
                false, false, false, false);
        //noinspection removal
        keyFileField.addBrowseFolderListener(nls("cfg.connection.title.SelectPrivateKeyFile"), "",
                null, fileChooserDescriptor);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    protected ActionListener createActionListener() {
        return e -> {
            getConfiguration().setModified(true);
            Object source = e.getSource();

            if (source == activeCheckBox) {
                enableDisableFields();
            }
        };
    }

    private void showHideFields() {
        boolean isKeyPair = getSelection(authTypeComboBox) == SshAuthType.KEY_PAIR;
        passwordField.setVisible(!isKeyPair);
        passwordLabel.setVisible(!isKeyPair);

        privateKeyFileLabel.setVisible(isKeyPair);
        privateKeyPassphraseLabel.setVisible(isKeyPair);
        keyFileField.setVisible(isKeyPair);
        keyPassphraseField.setVisible(isKeyPair);
    }

    private void enableDisableFields() {
        boolean enabled = activeCheckBox.isSelected();
        hostTextField.setEnabled(enabled);
        portTextField.setEnabled(enabled);
        userTextField.setEnabled(enabled);
        authTypeComboBox.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        passwordField.setBackground(enabled ? Colors.getTextFieldBackground() : Colors.getPanelBackground());
        keyFileField.setEnabled(enabled);
        keyPassphraseField.setEnabled(enabled);
        keyPassphraseField.setBackground(enabled ? Colors.getTextFieldBackground() : Colors.getPanelBackground());

    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        ConnectionSshTunnelSettings configuration = getConfiguration();
        applyFormChanges(configuration);
    }

    @Override
    public void applyFormChanges(ConnectionSshTunnelSettings configuration) throws ConfigurationException {
        boolean enabled = activeCheckBox.isSelected();
        configuration.setActive(enabled);
        configuration.setHost(ConfigurationEditors.validateStringValue(hostTextField, nls("cfg.connection.field.Host"), enabled));
        ConfigurationEditors.validateIntegerValue(portTextField, nls("cfg.connection.field.Port"), enabled, 0, 999999, null);
        configuration.setPort(portTextField.getText());
        configuration.setUser(userTextField.getText());
        SshAuthType authType = getSelection(authTypeComboBox);

        boolean isKeyPair = authType == SshAuthType.KEY_PAIR;
        ConfigurationEditors.validateStringValue(keyFileField.getTextField(), nls("cfg.connection.field.KeyFile"), enabled && isKeyPair);
        //ConfigurationEditorUtil.validateStringInputValue(keyPassphraseField, "Key passphrase", enabled && isKeyPair);

        configuration.setAuthType(authType);
        configuration.setPassword(String.valueOf(passwordField.getPassword()));
        configuration.setKeyFile(keyFileField.getText());
        configuration.setKeyPassphrase(String.valueOf(keyPassphraseField.getPassword()));
    }

    @Override
    public void resetFormChanges() {
        ConnectionSshTunnelSettings configuration = getConfiguration();
        activeCheckBox.setSelected(configuration.isActive());
        hostTextField.setText(configuration.getHost());
        portTextField.setText(configuration.getPort());
        userTextField.setText(configuration.getUser());
        passwordField.setText(configuration.getPassword());
        setSelection(authTypeComboBox, configuration.getAuthType());
        keyFileField.setText(configuration.getKeyFile());
        keyPassphraseField.setText(configuration.getKeyPassphrase());
    }
}
