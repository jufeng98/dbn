package com.dci.intellij.dbn.common.dispose;

import com.dci.intellij.dbn.common.util.Unsafe;

public interface StatefulDisposable extends com.intellij.openapi.Disposable {

    default boolean isDisposed() {
        return false;
    }

    default void setDisposed(boolean disposed) {

    }

    default void checkDisposed() {
        if (isDisposed()) throw new AlreadyDisposedException(this);
    }

    @Override
    default void dispose() {
        if (isDisposed()) return;
        setDisposed(true);

        Unsafe.warned(() -> disposeInner());
    }

    default void disposeInner() {}

    default void nullify() {
        Nullifier.nullify(this);
    }
}
