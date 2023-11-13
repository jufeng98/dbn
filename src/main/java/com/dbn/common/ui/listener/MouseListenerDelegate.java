package com.dbn.common.ui.listener;

import com.dbn.common.ref.WeakRef;
import lombok.experimental.Delegate;

import javax.swing.event.MenuListener;

public class MouseListenerDelegate implements MenuListener {
    private final WeakRef<MenuListener> delegate;

    public MouseListenerDelegate(MenuListener delegate) {
        this.delegate = WeakRef.of(delegate);
    }

    @Delegate
    private MenuListener getDelegate() {
        return delegate.ensure();
    }
}
