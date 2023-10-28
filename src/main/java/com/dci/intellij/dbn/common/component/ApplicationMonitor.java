package com.dci.intellij.dbn.common.component;

import com.dci.intellij.dbn.common.event.ApplicationEvents;
import com.intellij.ide.AppLifecycleListener;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ApplicationMonitor {
    private static final ApplicationMonitor INSTANCE = new ApplicationMonitor();

    private final AtomicBoolean exitRequested = new AtomicBoolean(false);
    private final AtomicBoolean exiting = new AtomicBoolean(false);

    private ApplicationMonitor() {
        ApplicationEvents.subscribe(null, AppLifecycleListener.TOPIC, new AppLifecycleListener() {
            @Override
            public void appWillBeClosed(boolean isRestart) {
                exiting.set(true);
            }

            @Override
            public void appClosing() {
                exitRequested.set(true);
            }
        });
    }

    public static boolean isAppExiting() {
        return INSTANCE.exiting.get();
    }

    public static boolean checkAppExitRequested() {
        return INSTANCE.exitRequested.getAndSet(false);
    }
}
