package com.dci.intellij.dbn.common.latent.impl;


import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.common.latent.Loader;
import com.dci.intellij.dbn.common.util.TimeUtil;

import java.util.concurrent.TimeUnit;

public class ReloadableLatent<T, M> extends BasicLatent<T> implements Latent<T> {
    private long timestamp;
    private final long intervalMillis;

    public ReloadableLatent(long interval, TimeUnit intervalUnit, Loader<T> loader) {
        super(loader);
        intervalMillis = intervalUnit.toMillis(interval);
    }

    @Override
    protected boolean shouldLoad(){
        return super.shouldLoad() || TimeUtil.isOlderThan(timestamp, intervalMillis);
    }

    @Override
    protected void beforeLoad() {
        timestamp = System.currentTimeMillis();
    }
}
