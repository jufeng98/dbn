package com.dbn.common.ui.list;

import com.dbn.common.ui.Presentable;

public interface Selectable<T> extends Presentable, Comparable<T> {
    String getError();
    boolean isSelected();
    boolean isMasterSelected();
    void setSelected(boolean selected);
}
