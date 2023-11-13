package com.dbn.connection.config.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.connection.config.ConnectionSslSettings;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ConnectionSslSettingsForm extends ConfigurationEditorForm<ConnectionSslSettings> {
    private JPanel mainPanel;
    private JPanel sshGroupPanel;
    private JCheckBox activeCheckBox;
    private TextFieldWithBrowseButton certificateAuthFileField;
    private TextFieldWithBrowseButton clientCertificateFile;
    private TextFieldWithBrowseButton clientKeyFile;

    public ConnectionSslSettingsForm(final ConnectionSslSettings configuration) {
        super(configuration);

        resetFormChanges();

        enableDisableFields();
        registerComponent(mainPanel);

        certificateAuthFileField.addBrowseFolderListener("Select certificate authority (CA) file", "",
                null, new FileChooserDescriptor(true, false, false, false, false, false));

        clientCertificateFile.addBrowseFolderListener("Select client certificate file", "",
                null, new FileChooserDescriptor(true, false, false, false, false, false));

        clientKeyFile.addBrowseFolderListener("Select client key file", "",
                null, new FileChooserDescriptor(true, false, false, false, false, false));
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

    private void enableDisableFields() {
        boolean enabled = activeCheckBox.isSelected();
        certificateAuthFileField.setEnabled(enabled);
        clientCertificateFile.setEnabled(enabled);
        clientKeyFile.setEnabled(enabled);

    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        ConnectionSslSettings configuration = getConfiguration();
        applyFormChanges(configuration);
    }

    @Override
    public void applyFormChanges(ConnectionSslSettings configuration) throws ConfigurationException {
        boolean enabled = activeCheckBox.isSelected();
        configuration.setActive(enabled);
        configuration.setCertificateAuthorityFile(certificateAuthFileField.getText());
        configuration.setClientCertificateFile(clientCertificateFile.getText());
        configuration.setClientKeyFile(clientKeyFile.getText());
    }

    @Override
    public void resetFormChanges() {
        ConnectionSslSettings configuration = getConfiguration();
        activeCheckBox.setSelected(configuration.isActive());
        certificateAuthFileField.setText(configuration.getCertificateAuthorityFile());
        clientCertificateFile.setText(configuration.getClientCertificateFile());
        clientKeyFile.setText(configuration.getClientKeyFile());
    }

}
