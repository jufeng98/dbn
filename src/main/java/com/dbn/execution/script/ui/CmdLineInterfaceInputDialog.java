package com.dbn.execution.script.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.execution.script.CmdLineInterface;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Set;

public class CmdLineInterfaceInputDialog extends DBNDialog<CmdLineInterfaceInputForm> {
    private final CmdLineInterface cmdLineInterface;
    private final Set<String> usedNames;

    public CmdLineInterfaceInputDialog(Project project, @NotNull CmdLineInterface cmdLineInterface, @NotNull Set<String> usedNames) {
        super(project, "Add command-line interface", true);
        this.cmdLineInterface = cmdLineInterface;
        this.usedNames = usedNames;
        setModal(true);
        renameAction(getOKAction(), "Save");
        init();
    }

    @NotNull
    @Override
    protected CmdLineInterfaceInputForm createForm() {
        return new CmdLineInterfaceInputForm(this, cmdLineInterface, usedNames);
    }

    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getOKAction(),
                getCancelAction(),
        };
    }

    void setActionEnabled(boolean enabled) {
        getOKAction().setEnabled(enabled);
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
