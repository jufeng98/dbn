package com.dbn.execution.compiler;

import com.dbn.common.option.InteractiveOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum CompileDependenciesOption implements InteractiveOption {
    YES(nls("cfg.compiler.const.DependenciesOption_YES"), true),
    NO(nls("cfg.compiler.const.DependenciesOption_NO"), true),
    ASK(nls("cfg.compiler.const.DependenciesOption_ASK"), false);

    private final String name;
    private final boolean persistable;

    @Override
    public boolean isCancel() {
        return false;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
