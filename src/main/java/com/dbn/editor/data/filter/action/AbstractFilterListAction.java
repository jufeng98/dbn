package com.dbn.editor.data.filter.action;

import com.dbn.common.action.BasicAction;
import com.dbn.editor.data.filter.DatasetFilterGroup;
import com.dbn.editor.data.filter.ui.DatasetFilterList;
import lombok.Getter;

import javax.swing.*;

@Getter
public abstract class AbstractFilterListAction extends BasicAction {
    private final DatasetFilterList filterList;

    protected AbstractFilterListAction(DatasetFilterList filterList, String name, Icon icon) {
        super(name, null, icon);
        this.filterList = filterList;
    }

    public DatasetFilterGroup getFilterGroup() {
        return (DatasetFilterGroup) filterList.getModel();
    }
}
