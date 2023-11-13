package com.dbn.editor.data.state.sorting.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.editor.data.DatasetEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DatasetEditorSortingDialog extends DBNDialog<DatasetEditorSortingForm> {
    private DatasetEditor datasetEditor;

    public DatasetEditorSortingDialog(@NotNull DatasetEditor datasetEditor) {
        super(datasetEditor.getProject(), "Sorting", true);
        this.datasetEditor = datasetEditor;
        setModal(true);
        setResizable(true);
        renameAction(getCancelAction(), "Cancel");
        init();
    }

    @NotNull
    @Override
    protected DatasetEditorSortingForm createForm() {
        return new DatasetEditorSortingForm(this, datasetEditor);
    }

    @Override
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
        getForm().applyChanges();
        datasetEditor.getEditorTable().sort();
        super.doOKAction();
    }
}
