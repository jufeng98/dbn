package com.dbn.execution.compiler.ui;

import com.dbn.common.text.TextContent;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.form.DBNHeaderForm;
import com.dbn.common.ui.form.DBNHintForm;
import com.dbn.object.common.DBSchemaObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.dbn.common.text.TextContent.plain;

public class CompilerTypeSelectionForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JCheckBox rememberSelectionCheckBox;
    private JPanel hintPanel;

    CompilerTypeSelectionForm(final CompilerTypeSelectionDialog parent, @Nullable DBSchemaObject object) {
        super(parent);
        if (object == null) {
            headerPanel.setVisible(false);
        } else {
            DBNHeaderForm headerForm = new DBNHeaderForm(this, object);
            headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
        }
        TextContent hintText = plain(
                "The compile option type \"Debug\" enables you to use the selected object(s) in debugging activities (i.e. pause/trace execution). " +
                        "For runtime performance reasons, it is recommended to use normal compile option, unless you plan to debug the selected element(s)." +
                        "\n\"Keep current\" will carry over the existing compile type.\n\n" +
                        "Please select your compile option.");
        DBNHintForm hintForm = new DBNHintForm(this, hintText, null, true);
        hintPanel.add(hintForm.getComponent(), BorderLayout.CENTER);

        parent.registerRememberSelectionCheckBox(rememberSelectionCheckBox);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

}
