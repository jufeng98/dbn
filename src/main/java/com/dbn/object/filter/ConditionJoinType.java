package com.dbn.object.filter;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum ConditionJoinType implements Presentable{
    AND,
    OR;


    @NotNull
    @Override
    public String getName() {
        return name();
    }
}
