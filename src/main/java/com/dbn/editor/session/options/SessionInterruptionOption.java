package com.dbn.editor.session.options;

import com.dbn.common.option.InteractiveOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum SessionInterruptionOption implements InteractiveOption{
    ASK(nls("app.sessions.const.SessionInterruptionOption_ASK"), null),
    NORMAL(nls("app.sessions.const.SessionInterruptionOption_NORMAL"), null),
    IMMEDIATE(nls("app.sessions.const.SessionInterruptionOption_IMMEDIATE"), null),
    POST_TRANSACTION(nls("app.sessions.const.SessionInterruptionOption_POST_TRANSACTION"), null),
    CANCEL(nls("app.sessions.const.SessionInterruptionOption_CANCEL"), null);

    private final String name;
    private final Icon icon;

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
