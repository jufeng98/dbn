package com.dbn.object.filter.custom.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.common.util.Dialogs;
import com.dbn.object.filter.custom.ObjectFilter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dbn.common.util.Conditional.when;

@Getter
public class ObjectFilterDetailsDialog extends DBNDialog<ObjectFilterDetailsForm> {
    private final ObjectFilter<?> filter;

    private ObjectFilterDetailsDialog(ObjectFilter<?> filter, boolean create) {
        super(filter.getProject(), getTitle(create), true);
        this.filter = filter;

        setModal(true);
        setResizable(true);
        Action okAction = getOKAction();

        renameAction(okAction, create ? "Create" : "Update");
        init();
    }

    public static void show(ObjectFilter<?> filter, boolean create, Runnable callback) {
        Dialogs.show(
                () -> new ObjectFilterDetailsDialog(filter, create),
                (dialog, exitCode) -> when(exitCode == 0, callback));
    }

    @NotNull
    @Override
    protected ObjectFilterDetailsForm createForm() {
        return new ObjectFilterDetailsForm(this);
    }

    private static String getTitle(boolean create) {
        return create ? "Create filter" : "Edit filter";
    }

    public void setActionEnabled(boolean enabled) {
        getOKAction().setEnabled(enabled);
    }

    @Override
    public void doOKAction() {
        ObjectFilterDetailsForm component = getForm();
        String expression = component.getExpression();
        filter.setExpression(expression);
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }
}
