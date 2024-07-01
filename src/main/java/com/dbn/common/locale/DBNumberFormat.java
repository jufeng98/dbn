package com.dbn.common.locale;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum DBNumberFormat implements Presentable{
    GROUPED(nls("cfg.shared.const.NumberFormat_GROUPED")),
    UNGROUPED(nls("cfg.shared.const.NumberFormat_UNGROUPED")),
    CUSTOM(nls("cfg.shared.const.NumberFormat_CUSTOM"));

    private final String name;
}
