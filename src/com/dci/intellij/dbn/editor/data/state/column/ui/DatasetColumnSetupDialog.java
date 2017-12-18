package com.dci.intellij.dbn.editor.data.state.column.ui;

import javax.swing.Action;
import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.DatasetLoadInstructions;
import com.intellij.openapi.project.Project;
import static com.dci.intellij.dbn.editor.data.DatasetLoadInstruction.*;

public class DatasetColumnSetupDialog extends DBNDialog<DatasetColumnSetupForm> {
    private static final DatasetLoadInstructions LOAD_INSTRUCTIONS = new DatasetLoadInstructions(USE_CURRENT_FILTER, PRESERVE_CHANGES, DELIBERATE_ACTION, REBUILD);
    private DatasetEditor datasetEditor;

    public DatasetColumnSetupDialog(Project project, DatasetEditor datasetEditor) {
        super(project, "Column setup", true);
        this.datasetEditor = datasetEditor;
        setModal(true);
        setResizable(true);
        component = new DatasetColumnSetupForm(project, datasetEditor);
        getCancelAction().putValue(Action.NAME, "Cancel");
        init();
    }

    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getOKAction(),
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        boolean changed = component.applyChanges();
        if (changed) {
            datasetEditor.loadData(LOAD_INSTRUCTIONS);
        }
        super.doOKAction();
    }

    @Override
    public void dispose() {
        super.dispose();
        datasetEditor = null;
    }
}
