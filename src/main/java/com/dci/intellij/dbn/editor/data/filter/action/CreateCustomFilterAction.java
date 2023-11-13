package com.dci.intellij.dbn.editor.data.filter.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.ui.DatasetFilterList;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CreateCustomFilterAction extends AbstractFilterListAction {

    public CreateCustomFilterAction(DatasetFilterList filterList) {
        super(filterList,  "Custom filter", Icons.DATASET_FILTER_CUSTOM);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DatasetFilter filter = getFilterGroup().createCustomFilter(true);
        getFilterList().setSelectedValue(filter, true);
    }
}