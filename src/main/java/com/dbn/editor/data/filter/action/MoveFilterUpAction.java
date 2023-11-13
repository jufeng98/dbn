package com.dbn.editor.data.filter.action;

import com.dbn.common.icon.Icons;
import com.dbn.editor.data.filter.DatasetFilter;
import com.dbn.editor.data.filter.ui.DatasetFilterList;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class MoveFilterUpAction extends AbstractFilterListAction {

    public MoveFilterUpAction(DatasetFilterList filterList) {
        super(filterList, "Move selection up", Icons.ACTION_MOVE_UP);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DatasetFilterList filterList = getFilterList();
        DatasetFilter filter = (DatasetFilter) filterList.getSelectedValue();
        getFilterGroup().moveFilterUp(filter);
        filterList.setSelectedIndex(filterList.getSelectedIndex()-1);
    }

    @Override
    public void update(AnActionEvent e) {
        int[] index = getFilterList().getSelectedIndices();
        e.getPresentation().setEnabled(index.length == 1 && index[0] > 0);
    }
}