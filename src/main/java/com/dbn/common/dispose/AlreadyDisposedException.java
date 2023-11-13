package com.dbn.common.dispose;

import com.intellij.openapi.progress.ProcessCanceledException;
import org.jetbrains.annotations.Nullable;

public class AlreadyDisposedException extends ProcessCanceledException {
    @Deprecated // TODO only use for disposed object checks / use constructor on runtime
    public static final AlreadyDisposedException INSTANCE = new AlreadyDisposedException();
    private AlreadyDisposedException() {};

    public AlreadyDisposedException(@Nullable Object o) {
        super(o == null ?
                new IllegalArgumentException("Object is null") :
                new IllegalStateException(o.getClass().getSimpleName() + " is invalid or disposed"));
    }
}
