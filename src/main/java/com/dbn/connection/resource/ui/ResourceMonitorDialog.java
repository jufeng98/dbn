package com.dbn.connection.resource.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.Action;

public class ResourceMonitorDialog extends DBNDialog<ResourceMonitorForm> {

    public ResourceMonitorDialog(Project project) {
        super(project, "Resource Monitor", true);
        setModal(false);
        setResizable(true);
        setCancelButtonText("Close");
        setDefaultSize(1000, 600);
        init();
    }

    @NotNull
    @Override
    protected ResourceMonitorForm createForm() {
        return new ResourceMonitorForm(this);
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    private DatabaseTransactionManager getTransactionManager() {
        return DatabaseTransactionManager.getInstance(getProject());
    }
}
