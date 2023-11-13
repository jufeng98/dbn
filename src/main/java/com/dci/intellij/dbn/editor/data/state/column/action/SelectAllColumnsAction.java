package com.dci.intellij.dbn.editor.data.state.column.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.list.CheckBoxList;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class SelectAllColumnsAction extends AnAction {
    private final CheckBoxList list;

    public SelectAllColumnsAction(CheckBoxList list)  {
        super("Select All Columns", null, Icons.ACTION_SELECT_ALL);
        this.list = list;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        list.selectAll();
    }
}
