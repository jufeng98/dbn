package com.dbn.editor.code.options;

import com.dbn.common.option.InteractiveOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum CodeEditorChangesOption implements InteractiveOption {
    ASK(nls("cfg.codeEditor.const.ChangesOption_ASK")),
    SAVE(nls("cfg.codeEditor.const.ChangesOption_SAVE")),
    DISCARD(nls("cfg.codeEditor.const.ChangesOption_DISCARD")),
    SHOW(nls("cfg.codeEditor.const.ChangesOption_SHOW")),
    CANCEL(nls("cfg.codeEditor.const.ChangesOption_CANCEL"));

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
