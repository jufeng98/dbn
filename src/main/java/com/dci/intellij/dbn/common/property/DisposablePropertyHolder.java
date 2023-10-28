package com.dci.intellij.dbn.common.property;

import com.dci.intellij.dbn.common.dispose.StatefulDisposable;

public abstract class DisposablePropertyHolder<T extends Property.IntBase> extends PropertyHolderBase.IntStore<T> implements StatefulDisposable {

    @Override
    public final boolean isDisposed() {
        return is(getDisposedProperty());
    }

    @Override
    public void dispose() {
        if (isDisposed()) return;
        set(getDisposedProperty(), true);
        disposeInner();
    }

    protected abstract T getDisposedProperty();
}
