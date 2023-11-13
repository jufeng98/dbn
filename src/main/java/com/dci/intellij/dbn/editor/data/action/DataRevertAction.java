package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.model.DatasetEditorModelCell;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DataRevertAction extends AnAction{
    private final DatasetEditorModelCell cell;

    public DataRevertAction(DatasetEditorModelCell cell) {
        super("Revert Changes", null, Icons.ACTION_REVERT_CHANGES);
        this.cell = cell;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        cell.revertChanges();
    }


}
