package com.dbn.execution;

import com.dbn.common.option.InteractiveOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@Deprecated
@AllArgsConstructor
public enum TargetConnectionOption implements InteractiveOption{
    ASK(nls("cfg.execution.const.TargetConnectionOption_ASK")),
    MAIN(nls("cfg.execution.const.TargetConnectionOption_MAIN")),
    POOL(nls("cfg.execution.const.TargetConnectionOption_POOL")),
    CANCEL(nls("cfg.execution.const.TargetConnectionOption_CANCEL"));

    private final String name;

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
