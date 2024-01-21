package com.dbn.debugger.common.config.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.debugger.common.config.DBRunConfig;
import com.dbn.debugger.common.process.ui.CompileDebugDependenciesForm;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.lookup.DBObjectRef;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CompileDebugDependenciesDialog extends DBNDialog<CompileDebugDependenciesForm> {
    private final DBRunConfig runConfiguration;
    private final List<DBSchemaObject> compileList;

    //@Sticky
    private DBObjectRef<DBSchemaObject>[] selection;

    public CompileDebugDependenciesDialog(DBRunConfig runConfiguration, List<DBSchemaObject> compileList) {
        super(runConfiguration.getProject(), "Compile object dependencies", true);
        this.runConfiguration = runConfiguration;
        this.compileList = compileList;
        init();
    }

    @NotNull
    @Override
    protected CompileDebugDependenciesForm createForm() {
        return new CompileDebugDependenciesForm(this, runConfiguration, compileList);
    }

    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                new CompileAllAction(),
                new CompileSelectedAction(),
                new CompileNoneAction(),
                getCancelAction()
        };
    }

    private class CompileSelectedAction extends AbstractAction {
        private CompileSelectedAction() {
            super("Compile selected");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doOKAction();
        }
    }

    private class CompileAllAction extends AbstractAction {
        private CompileAllAction() {
            super("Compile all");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getForm().selectAll();
            doOKAction();
        }
    }

    private class CompileNoneAction extends AbstractAction {
        private CompileNoneAction() {
            super("Compile none");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getForm().selectNone();
            doOKAction();
        }
    }

    @Override
    protected void doOKAction() {
        selection = getForm().getSelection().toArray(new DBObjectRef[0]);
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    public DBObjectRef<DBSchemaObject>[] getSelection() {
        return selection;
    }
}
