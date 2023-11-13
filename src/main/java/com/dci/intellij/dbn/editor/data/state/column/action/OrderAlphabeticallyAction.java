package com.dci.intellij.dbn.editor.data.state.column.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.list.CheckBoxList;
import com.dci.intellij.dbn.editor.data.state.column.ui.ColumnStateSelectable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class OrderAlphabeticallyAction extends AnAction {
    private final CheckBoxList list;

    public OrderAlphabeticallyAction(CheckBoxList list)  {
        super("Order Columns Alphabetically", null, Icons.ACTION_SORT_ALPHA);
        this.list = list;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        list.sortElements(ColumnStateSelectable.NAME_COMPARATOR);
    }
}
