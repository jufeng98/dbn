package com.dbn.common.latent.impl;


import com.dbn.common.latent.Latent;
import com.dbn.common.latent.Loader;

import java.util.Objects;

public class MutableLatent<T, M> extends BasicLatent<T> implements Latent<T> {
    private M mutable;
    private final Loader<M> mutableLoader;

    public MutableLatent(Loader<M> mutableLoader, Loader<T> loader) {
        super(loader);
        this.mutableLoader = mutableLoader;
    }

    @Override
    protected boolean shouldLoad(){
        return super.shouldLoad() || (mutable != null && !Objects.equals(mutable, mutableLoader.load()));
    }

    @Override
    protected void beforeLoad() {
        mutable = mutableLoader.load();
    }
}
