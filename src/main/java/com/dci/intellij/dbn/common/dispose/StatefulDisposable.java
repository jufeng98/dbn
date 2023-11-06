package com.dci.intellij.dbn.common.dispose;

import com.dci.intellij.dbn.common.util.Unsafe;

public interface StatefulDisposable extends com.intellij.openapi.Disposable {

    boolean isDisposed();

    void setDisposed(boolean disposed);

    void disposeInner();

    default void checkDisposed() {
        if (isDisposed()) throw new AlreadyDisposedException(this);
    }

    @Override
    default void dispose() {
        if (isDisposed()) return;
        setDisposed(true);

        Unsafe.warned(() -> disposeInner());
    }

    default void nullify() {
        Nullifier.nullify(this);
    }
}
