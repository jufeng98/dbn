package com.dci.intellij.dbn.common.dispose;

import com.dci.intellij.dbn.common.util.Unsafe;
import com.intellij.openapi.Disposable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class StatefulDisposableBase implements StatefulDisposable {
    private boolean disposed;

    public StatefulDisposableBase() {
    }

    public StatefulDisposableBase(@Nullable Disposable parent) {
        if (Checks.isValid(parent)) {
            Disposer.register(parent, this);
        }
    }

    @Override
    public final void dispose() {
        if (disposed) return;
        disposed = true;

        Unsafe.warned(() -> disposeInner());
    }

}
