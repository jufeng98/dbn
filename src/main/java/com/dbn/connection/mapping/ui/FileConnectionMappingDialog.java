package com.dbn.connection.mapping.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.Action;

public class FileConnectionMappingDialog extends DBNDialog<FileConnectionMappingForm> {

    public FileConnectionMappingDialog(Project project) {
        super(project, "File connection mappings", true);
        setModal(false);
        setResizable(true);
        setDefaultSize(1200, 700);
        renameAction(getCancelAction(), "Close");
        init();
    }

    @NotNull
    @Override
    protected FileConnectionMappingForm createForm() {
        return new FileConnectionMappingForm(this);
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
    public void doCancelAction() {
        super.doCancelAction();
    }
}
