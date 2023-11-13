package com.dbn.browser.options;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum BrowserDisplayMode implements Presentable{

    @Deprecated SINGLE("Single tree"),
    SIMPLE("Single tree"),
    TABBED("Multiple connection tabs");

    private String name;

    BrowserDisplayMode(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
