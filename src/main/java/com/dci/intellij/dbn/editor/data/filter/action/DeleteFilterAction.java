package com.dci.intellij.dbn.editor.data.filter.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.ui.DatasetFilterList;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeleteFilterAction extends AbstractFilterListAction {

    public DeleteFilterAction(DatasetFilterList filterList) {
        super(filterList, "Delete filter", Icons.ACTION_REMOVE);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<DatasetFilter> selectedFilters = getFilterList().getSelectedValuesList();
        for (DatasetFilter filter : selectedFilters) {
            getFilterGroup().deleteFilter(filter);
            if (getFilterList().getModel().getSize() > 0) {
                getFilterList().setSelectedIndex(0);
            }

        }
    }
}
