package com.dbn.common.icon;

import com.dbn.common.latent.Latent;
import lombok.Getter;
import lombok.experimental.Delegate;

import javax.swing.*;

@Getter
abstract class LatentIcon implements Icon {
    private final String path;
    private final Latent<Icon> delegate = Latent.basic(() -> load());

    public LatentIcon(String path) {
        this.path = path;
    }

    protected abstract Icon load();

    @Delegate
    public Icon getDelegate() {
        return delegate.get();
    }
}
