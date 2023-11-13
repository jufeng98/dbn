package com.dbn.plugin.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.plugin.PluginConflictManager;
import com.dbn.plugin.PluginConflictResolution;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PluginConflictResolutionDialog extends DBNDialog<PluginConflictResolutionForm> {
    public PluginConflictResolutionDialog() {
        super(null, "Plugin Conflict Resolution", true);
        setModal(true);
        setResizable(false);
        //setDefaultSize(700, 400);
        getCancelAction().setEnabled(false);
        renameAction(getOKAction(), "Continue");
        init();
    }


    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    @NotNull
    @Override
    protected PluginConflictResolutionForm createForm() {
        return new PluginConflictResolutionForm(this);
    }

    protected void renameAction(String name) {
        renameAction(getOKAction(), name);
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getOKAction()
        };
    }

    @Override
    protected void doOKAction() {
        PluginConflictResolutionForm resolutionForm = getForm();
        PluginConflictResolution resolution = resolutionForm.getChosenResolution();
        if (resolution == null) {
            resolutionForm.showErrorMessage();
            return;
        }

        PluginConflictManager conflictManager = PluginConflictManager.getInstance();
        conflictManager.applyConflictResolution(resolution);
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        // do not allow closing the dialog from X
    }
}
