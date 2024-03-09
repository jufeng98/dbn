package com.dbn.object.filter.custom.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.object.filter.custom.ObjectFilter;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Getter
public class ObjectFilterDetailsDialog extends DBNDialog<ObjectFilterDetailsForm> {
    private final ObjectFilter filter;

    public ObjectFilterDetailsDialog(ObjectFilter filter, boolean create) {
        super(filter.getProject(), getTitle(create), true);
        this.filter = filter;

        setModal(true);
        setResizable(true);
        Action okAction = getOKAction();

        renameAction(okAction, create ? "Create" : "Update");
        init();
    }

    @NotNull
    @Override
    protected ObjectFilterDetailsForm createForm() {
        return new ObjectFilterDetailsForm(this);
    }

    private static String getTitle(boolean create) {
        return create ? "Create filter" : "Edit filter";
    }

    @Override
    public void doOKAction() {
        ObjectFilterDetailsForm component = getForm();
        // apply
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }
}
