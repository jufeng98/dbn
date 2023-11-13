package com.dci.intellij.dbn.connection.console.ui;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.form.DBNHeaderForm;
import com.dci.intellij.dbn.common.util.Naming;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.object.DBConsole;
import com.dci.intellij.dbn.vfs.DBConsoleType;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Set;

import static com.dci.intellij.dbn.common.ui.util.TextFields.onTextChange;

public class CreateRenameConsoleForm extends DBNFormBase {
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JTextField consoleNameTextField;
    private JLabel errorLabel;

    private final ConnectionRef connection;
    private final DBConsoleType consoleType;
    private final DBConsole console;

    CreateRenameConsoleForm(final CreateRenameConsoleDialog parent, @NotNull ConnectionHandler connection, @Nullable final DBConsole console, DBConsoleType consoleType) {
        super(parent);
        this.connection = connection.ref();
        this.console = console;
        this.consoleType = consoleType;
        errorLabel.setForeground(JBColor.RED);
        errorLabel.setIcon(Icons.EXEC_MESSAGES_ERROR);
        errorLabel.setVisible(false);

        DBNHeaderForm headerForm = console == null ?
                new DBNHeaderForm(this, "[New " + consoleType.getName() + "]", consoleType.getIcon(), connection.getEnvironmentType().getColor()) :
                new DBNHeaderForm(this, console);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);

        Set<String> consoleNames = connection.getConsoleBundle().getConsoleNames();

        String name;
        if (console == null) {
            name = connection.getName() + " 1";
            while (consoleNames.contains(name)) {
                name = Naming.nextNumberedIdentifier(name, true);
            }
        } else {
            name = console.getName();
            consoleNames.remove(name);
            parent.getOKAction().setEnabled(false);
        }
        consoleNameTextField.setText(name);

        onTextChange(consoleNameTextField, e -> updateErrorMessage());
    }

    private void updateErrorMessage() {
        Set<String> consoleNames = getConnection().getConsoleBundle().getConsoleNames();
        String text = Strings.trim(consoleNameTextField.getText());

        String errorText = null;
        if (Strings.isEmpty(text)) {
            errorText = "Console name must be specified";
        }
        else if (consoleNames.contains(text)) {
            errorText = "Console name already in use";
        }


        errorLabel.setVisible(errorText != null);
        CreateRenameConsoleDialog parent = ensureParentComponent();
        parent.getOKAction().setEnabled(errorText == null && (console == null || !Objects.equals(console.getName(), text)));
        if (errorText != null) {
            errorLabel.setText(errorText);
        }
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return consoleNameTextField;
    }

    public String getConsoleName() {
        return consoleNameTextField.getText();
    }

    public DBConsoleType getConsoleType() {
        return console == null ? consoleType : console.getConsoleType();
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    public DBConsole getConsole() {
        return console;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

}
