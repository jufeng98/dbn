package com.dbn.editor.session.options;

import com.dbn.common.option.InteractiveOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.nls.NlsResources.nls;

public enum SessionInterruptionOption implements InteractiveOption{
    ASK(nls("app.sessionBrowser.const.SessionInterruptionOption_ASK"), null),
    NORMAL(nls("app.sessionBrowser.const.SessionInterruptionOption_NORMAL"), null),
    IMMEDIATE(nls("app.sessionBrowser.const.SessionInterruptionOption_IMMEDIATE"), null),
    POST_TRANSACTION(nls("app.sessionBrowser.const.SessionInterruptionOption_POST_TRANSACTION"), null),
    CANCEL(nls("app.sessionBrowser.const.SessionInterruptionOption_CANCEL"), null);

    private final String name;
    private final Icon icon;

    SessionInterruptionOption(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
