package com.dci.intellij.dbn.object.factory.ui;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.form.DBNForm;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.type.ui.DataTypeEditor;
import com.dci.intellij.dbn.object.factory.ArgumentFactoryInput;
import com.dci.intellij.dbn.object.factory.ObjectFactoryInput;
import com.dci.intellij.dbn.object.factory.ui.common.ObjectFactoryInputForm;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArgumentFactoryInputForm extends ObjectFactoryInputForm<ArgumentFactoryInput> {
    private JPanel mainPanel;
    private JLabel iconLabel;
    private JTextField nameTextField;
    private JCheckBox inCheckBox;
    private JCheckBox outCheckBox;
    private JPanel dataTypeEditor;
    private final boolean enforceInArgument;

    ArgumentFactoryInputForm(DBNForm parent, ConnectionHandler connection, boolean enforceInArgument, int index) {
        super(parent, connection, DBObjectType.ARGUMENT, index);
        this.enforceInArgument = enforceInArgument;
        iconLabel.setText(null);
        iconLabel.setIcon(enforceInArgument ? Icons.DBO_ARGUMENT_IN : DBObjectType.ARGUMENT.getIcon());
        if (enforceInArgument) {
            inCheckBox.setVisible(false);
            outCheckBox.setVisible(false);
        } else {
            inCheckBox.addActionListener(actionListener);
            outCheckBox.addActionListener(actionListener);
        }
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == inCheckBox || e.getSource() == outCheckBox) {
                Icon icon =
                     inCheckBox.isSelected() && outCheckBox.isSelected() ? Icons.DBO_ARGUMENT_IN_OUT :
                     inCheckBox.isSelected() ? Icons.DBO_ARGUMENT_IN :
                     outCheckBox.isSelected() ? Icons.DBO_ARGUMENT_OUT : Icons.DBO_ARGUMENT;

                iconLabel.setIcon(icon);
            }
        }
    };

    @Override
    public ArgumentFactoryInput createFactoryInput(ObjectFactoryInput parent) {
        return new ArgumentFactoryInput(
                parent,
                getIndex(),
                nameTextField.getText(),
                ((DataTypeEditor) dataTypeEditor).getDataTypeRepresentation(),
                enforceInArgument || inCheckBox.isSelected(),
                outCheckBox.isSelected());
    }

    @Override
    public void focus() {
        nameTextField.requestFocus();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    private void createUIComponents() {
        dataTypeEditor = new DataTypeEditor(getConnection());
    }
}
