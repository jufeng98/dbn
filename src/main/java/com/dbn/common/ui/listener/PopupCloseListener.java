package com.dbn.common.ui.listener;

import com.dbn.common.dispose.Disposer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import org.jetbrains.annotations.NotNull;

public class PopupCloseListener implements JBPopupListener {
    private final Disposable content;

    private PopupCloseListener(@NotNull Disposable content) {
        this.content = content;
    }

    public static PopupCloseListener create(@NotNull Disposable disposable) {
        return new PopupCloseListener(disposable);
    }

    @Override
    public void onClosed(@NotNull LightweightWindowEvent event) {
        Disposer.dispose(content);
    }
}
