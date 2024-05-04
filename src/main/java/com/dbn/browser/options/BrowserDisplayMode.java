package com.dbn.browser.options;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

public enum BrowserDisplayMode implements Presentable{

    SIMPLE("Single Tree"),
    TABBED("Connection Tabs"),
    SELECTOR("Connection Selector");

    private final String name;

    BrowserDisplayMode(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
