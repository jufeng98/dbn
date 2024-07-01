package com.dbn.common.option;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum YesNoOption implements InteractiveOption {
    YES(nls("cfg.shared.const.YesNoOption_YES"), true),
    NO(nls("cfg.shared.const.YesNoOption_NO"), true);

    private final String name;
    private final boolean persistable;

    @Override
    public boolean isCancel() {
        return false;
    }

    @Override
    public boolean isAsk() {
        return false;
    }
}
