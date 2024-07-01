package com.dbn.code.common.style.options;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum CodeStyleCase implements Presentable{
    PRESERVE (nls("cfg.codeStyle.const.CodeStyleCase_PRESERVE")),
    UPPER(nls("cfg.codeStyle.const.CodeStyleCase_UPPER")),
    LOWER(nls("cfg.codeStyle.const.CodeStyleCase_LOWER")),
    CAPITALIZED(nls("cfg.codeStyle.const.CodeStyleCase_CAPITALIZED"));

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
