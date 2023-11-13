package com.dbn.object.common.sorting;

import com.dbn.common.ui.Presentable;
import lombok.Getter;

@Getter
public enum SortingType implements Presentable{
    NAME("Name"),
    POSITION("Position");

    private final String name;

    SortingType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
