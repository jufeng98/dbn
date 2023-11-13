package com.dbn.common.dispose;

import com.dbn.common.util.Unsafe;

import javax.swing.*;

public interface StatefulDisposable extends com.intellij.openapi.Disposable {
    JPanel DISPOSED_COMPONENT = new JPanel();

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
