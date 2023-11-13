package com.dbn.common.ui.form;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.common.dispose.Disposer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public abstract class DBNContentWithHeaderForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;

    private final DBNHeaderForm headerForm;
    private final DBNForm contentForm;

    protected DBNContentWithHeaderForm(@NotNull DBNDialog<?> parent) {
        super(parent);
        headerForm = createHeaderForm();
        contentForm = createContentForm();

        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
        contentPanel.add(contentForm.getComponent(), BorderLayout.CENTER);

        Disposer.register(this, headerForm);
        Disposer.register(this, contentForm);
    }

    public abstract DBNHeaderForm createHeaderForm();

    public abstract DBNForm createContentForm();

    public DBNHeaderForm getHeaderForm() {
        return headerForm;
    }

    public DBNForm getContentForm() {
        return contentForm;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
