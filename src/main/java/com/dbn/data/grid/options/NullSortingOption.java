package com.dbn.data.grid.options;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum NullSortingOption implements Presentable{
    FIRST(nls("cfg.data.const.NullSortingOption_FIRST")),
    LAST(nls("cfg.data.const.NullSortingOption_LAST"));

    private final String name;
}
