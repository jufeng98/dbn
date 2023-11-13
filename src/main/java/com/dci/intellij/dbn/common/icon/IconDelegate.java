package com.dci.intellij.dbn.common.icon;

import com.dci.intellij.dbn.common.latent.Latent;
import lombok.Getter;
import lombok.experimental.Delegate;

import javax.swing.*;

@Getter
public abstract class IconDelegate implements Icon {
    private final String path;
    private final Latent<Icon> delegate = Latent.basic(() -> load());

    public IconDelegate(String path) {
        this.path = path;
    }

    protected abstract Icon load();

    @Delegate
    public Icon getDelegate() {
        return delegate.get();
    }
}
