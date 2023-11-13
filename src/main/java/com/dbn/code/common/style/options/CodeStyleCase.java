package com.dbn.code.common.style.options;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum CodeStyleCase implements Presentable{
    PRESERVE ("Preserve case"),
    UPPER("Upper case"),
    LOWER("Lower case"),
    CAPITALIZED("Capitalized");

    private String name;

    private CodeStyleCase(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }
}
