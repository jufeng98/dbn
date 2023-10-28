package com.dci.intellij.dbn.common.dispose;

import com.dci.intellij.dbn.common.component.ApplicationMonitor;
import com.dci.intellij.dbn.common.thread.Background;
import com.dci.intellij.dbn.common.thread.ThreadMonitor;
import com.dci.intellij.dbn.common.thread.ThreadProperty;
import com.dci.intellij.dbn.diagnostics.Diagnostics;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.common.thread.ThreadMonitor.isDisposerProcess;

@Slf4j
@Getter
@Setter
public final class BackgroundDisposer {
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private volatile boolean running;

    private static final BackgroundDisposer INSTANCE = new BackgroundDisposer();

    private BackgroundDisposer() {}

    public static void queue(Runnable runnable) {
        if (isDisposerProcess() || Diagnostics.isBackgroundDisposerDisabled()) {
            runnable.run();
        } else {
            INSTANCE.push(runnable);
        }

    }

    private boolean isCancelled() {
        return ApplicationMonitor.isAppExiting();
    }

    private void push(Runnable runnable) {
        if (isCancelled()) return;
        queue.add(runnable);
        
        if (running || isCancelled()) return;

        synchronized (this) {
            if (running || isCancelled()) return;
            running = true;
            start();
        }
    }

    private void start() {
        Background.run(null, () -> {
            try {
                ThreadMonitor.wrap(ThreadProperty.DISPOSER, () -> dispose());
            } finally {
                running = false;
            }
        });
    }

    private void dispose() throws InterruptedException {
        while (!isCancelled()) {
            Runnable task = queue.poll(10, TimeUnit.SECONDS);
            if (task == null) continue;
            try {
                guarded(() -> task.run());
            } catch (Exception e) {
                log.error("Background disposer failed", e);
            }
        }
    }
}
