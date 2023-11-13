package com.dbn.execution.script.ui;

import com.dbn.common.icon.Icons;
import com.dbn.common.text.TextContent;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.form.DBNHintForm;
import com.dbn.common.util.Strings;
import com.dbn.connection.DatabaseType;
import com.dbn.execution.script.CmdLineInterface;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

import static com.dbn.common.ui.util.TextFields.onTextChange;

public class CmdLineInterfaceInputForm extends DBNFormBase {
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JLabel databaseTypeLabel;
    private JLabel nameInUseLabel;
    private JPanel executablePanel;
    private JLabel executableLabel;
    private JPanel databaseTypePanel;
    private JPanel hintPanel;

    public CmdLineInterfaceInputForm(@NotNull CmdLineInterfaceInputDialog parent, @NotNull CmdLineInterface cmdLineInterface, @NotNull Set<String> usedNames) {
        super(parent);
        DatabaseType databaseType = cmdLineInterface.getDatabaseType();
        databaseTypeLabel.setText(databaseType.name());
        databaseTypeLabel.setIcon(databaseType.getIcon());
        databaseTypePanel.setBorder(UIUtil.getTextFieldBorder());

        String executablePath = cmdLineInterface.getExecutablePath();
        executableLabel.setText(executablePath);
        executablePanel.setBorder(UIUtil.getTextFieldBorder());

        nameTextField.setText(CmdLineInterface.getDefault(databaseType).getName());
        nameInUseLabel.setIcon(Icons.COMMON_ERROR);

        cmdLineInterface.setDatabaseType(databaseType);
        cmdLineInterface.setExecutablePath(executablePath);
        onTextChange(nameTextField, e -> updateComponents(cmdLineInterface, usedNames));

        TextContent hintText =TextContent.plain(
                "Please provide a name for storing Command-Line interface executable.\n" +
                "Command-Line interfaces can be configured in DBN Settings > Execution Engine > Script Execution.");
        DBNHintForm hintForm = new DBNHintForm(this, hintText, null, true);
        hintPanel.add(hintForm.getComponent(), BorderLayout.CENTER);

        updateComponents(cmdLineInterface, usedNames);
    }

    public CmdLineInterfaceInputDialog getParentDialog() {
        return ensureParentComponent();
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return nameTextField;
    }

    private void updateComponents(CmdLineInterface cmdLineInterface, Set<String> usedNames) {
        String name = nameTextField.getText();
        cmdLineInterface.setName(name);
        boolean isNameUsed = usedNames.contains(name);
        nameInUseLabel.setVisible(isNameUsed);

        CmdLineInterfaceInputDialog parentComponent = getParentDialog();
        parentComponent.setActionEnabled(!isNameUsed && Strings.isNotEmpty(nameTextField.getText()));
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
