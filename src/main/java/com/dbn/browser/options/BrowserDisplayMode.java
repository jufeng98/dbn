package com.dbn.browser.options;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;


@Getter
@AllArgsConstructor
public enum BrowserDisplayMode implements Presentable{
    SIMPLE(nls("app.browser.const.DisplayMode_SIMPLE")),
    TABBED(nls("app.browser.const.DisplayMode_TABBED")),
    SELECTOR(nls("app.browser.const.DisplayMode_SELECTOR"));

    private final String name;
}
