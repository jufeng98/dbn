package com.dbn.connection.config.action;

import com.dbn.common.action.ContextAction;
import com.dbn.common.action.DataKeys;
import com.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class ConnectionSettingsAction extends ContextAction<ConnectionBundleSettingsForm> {
    public ConnectionSettingsAction(String text, Icon icon) {
        super(text, null, icon);
    }

    @Nullable
    protected ConnectionBundleSettingsForm getTarget(@NotNull AnActionEvent e) {
        return e.getData((DataKeys.CONNECTION_BUNDLE_SETTINGS));
    }
}
