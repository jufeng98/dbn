package com.dbn.object.filter;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum ConditionOperator implements Presentable{
    EQUAL("equal", false),
    NOT_EQUAL("not equal", false),
    LIKE("like", true),
    NOT_LIKE("not like", true);

    private final String name;
    private final boolean allowsWildcards;

    ConditionOperator(String text, boolean allowsWildcards) {
        this.name = text;
        this.allowsWildcards = allowsWildcards;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    public boolean allowsWildcards() {
        return allowsWildcards;
    }

    @Override
    public String toString() {
        return name;
    }

}
