package com.dci.intellij.dbn.connection.session.ui;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.form.DBNHeaderForm;
import com.dci.intellij.dbn.common.util.Naming;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Set;

import static com.dci.intellij.dbn.common.ui.util.TextFields.onTextChange;

public class CreateRenameSessionForm extends DBNFormBase {
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JTextField sessionNameTextField;
    private JLabel errorLabel;

    private final ConnectionRef connection;
    private DatabaseSession session;

    CreateRenameSessionForm(CreateRenameSessionDialog parent, @NotNull ConnectionHandler connection, @Nullable final DatabaseSession session) {
        super(parent);
        this.connection = connection.ref();
        this.session = session;
        errorLabel.setForeground(JBColor.RED);
        errorLabel.setIcon(Icons.EXEC_MESSAGES_ERROR);
        errorLabel.setVisible(false);

        DBNHeaderForm headerForm = new DBNHeaderForm(this, connection);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);

        Set<String> sessionNames = connection.getSessionBundle().getSessionNames();

        String name;
        if (session == null) {
            name = "Session 1";
            while (sessionNames.contains(name)) {
                name = Naming.nextNumberedIdentifier(name, true);
            }
        } else {
            name = session.getName();
            sessionNames.remove(name);
            parent.getOKAction().setEnabled(false);
        }
        sessionNameTextField.setText(name);
        onTextChange(sessionNameTextField, e -> updateErrorMessage());
    }

    private void updateErrorMessage() {
        Set<String> sessionNames = getConnection().getSessionBundle().getSessionNames();

        String errorText = null;
        String text = Strings.trim(sessionNameTextField.getText());

        if (Strings.isEmpty(text)) {
            errorText = "Session name must be specified";
        }
        else if (sessionNames.contains(text)) {
            errorText = "Session name already in use";
        }

        errorLabel.setVisible(errorText != null);
        CreateRenameSessionDialog parent = ensureParentComponent();
        parent.getOKAction().setEnabled(errorText == null && (session == null || !Objects.equals(session.getName(), text)));
        if (errorText != null) {
            errorLabel.setText(errorText);
        }
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return sessionNameTextField;
    }

    public String getSessionName() {
        return sessionNameTextField.getText();
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    public DatabaseSession getSession() {
        return session;
    }

    public void setSession(DatabaseSession session) {
        this.session = session;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

}
