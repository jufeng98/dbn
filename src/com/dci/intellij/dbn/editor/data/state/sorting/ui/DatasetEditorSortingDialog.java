package com.dci.intellij.dbn.editor.data.state.sorting.ui;

import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DatasetEditorSortingDialog extends DBNDialog<DatasetEditorSortingForm> {
    private DatasetEditor datasetEditor;

    public DatasetEditorSortingDialog(@NotNull DatasetEditor datasetEditor) {
        super(datasetEditor.getProject(), "Sorting", true);
        this.datasetEditor = datasetEditor;
        setModal(true);
        setResizable(true);
        getCancelAction().putValue(Action.NAME, "Cancel");
        init();
    }

    @NotNull
    @Override
    protected DatasetEditorSortingForm createComponent() {
        return new DatasetEditorSortingForm(this, datasetEditor);
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
        getComponent().applyChanges();
        datasetEditor.getEditorTable().sort();
        super.doOKAction();
    }

    @Override
    public void dispose() {
        super.dispose();
        datasetEditor = null;
    }
}
