package com.dbn.browser.options;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

import static com.dbn.nls.NlsResources.nls;


public enum BrowserDisplayMode implements Presentable{

    SIMPLE(nls("app.databaseBrowser.const.DisplayMode_SIMPLE")),
    TABBED(nls("app.databaseBrowser.const.DisplayMode_TABBED")),
    SELECTOR(nls("app.databaseBrowser.const.DisplayMode_SELECTOR"));

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
