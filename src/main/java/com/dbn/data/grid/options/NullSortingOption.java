package com.dbn.data.grid.options;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum NullSortingOption implements Presentable{
    FIRST("FIRST"),
    LAST("LAST");

    String name;

    NullSortingOption(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
