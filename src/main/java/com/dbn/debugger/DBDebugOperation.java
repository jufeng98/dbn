package com.dbn.debugger;

import com.dbn.common.notification.NotificationGroup;
import com.dbn.common.notification.NotificationSupport;
import com.dbn.common.thread.Threads;
import com.dbn.database.interfaces.DatabaseInterface.Runnable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public interface DBDebugOperation {

    static <T> void run(@NotNull Project project, String title, Runnable runnable) {
        ExecutorService executorService = Threads.debugExecutor();
        executorService.submit( () -> {
            Thread currentThread = Thread.currentThread();
            int initialPriority = currentThread.getPriority();
            currentThread.setPriority(Thread.MIN_PRIORITY);
            try {
                runnable.run();
            } catch (Exception e) {
                conditionallyLog(e);
                NotificationSupport.sendErrorNotification(
                        project,
                        NotificationGroup.DEBUGGER,
                        "Error performing debug operation ({0}): {1}", title, e);
            } finally {
                currentThread.setPriority(initialPriority);
            }
        });
    }


}
