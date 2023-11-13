package com.dbn.common.thread;

import com.intellij.openapi.progress.ProgressIndicator;

@FunctionalInterface
public interface ProgressRunnable {
    void run(ProgressIndicator progress);
}
