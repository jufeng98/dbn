package com.dbn.common.exception;

import com.intellij.openapi.progress.ProcessCanceledException;

public class OutdatedContentException extends ProcessCanceledException {
    public OutdatedContentException(Object o) {
        super(new IllegalStateException(o.getClass().getSimpleName() + " is outdated"));
    }
}
