package com.dbn.data.record.navigation;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum RecordNavigationTarget implements Presentable{
    VIEWER(nls("cfg.data.const.RecordNavigationTarget_VIEWER"), null),
    EDITOR(nls("cfg.data.const.RecordNavigationTarget_EDITOR"), null),
    ASK(nls("cfg.data.const.RecordNavigationTarget_ASK"), null),
    PROMPT(nls("cfg.data.const.RecordNavigationTarget_PROMPT"), null);

    private final String name;
    private final Icon icon;
}
