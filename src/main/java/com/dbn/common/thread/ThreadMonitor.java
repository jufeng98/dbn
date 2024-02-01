package com.dbn.common.thread;

import com.dbn.common.load.ProgressMonitor;
import com.dbn.common.routine.ThrowableCallable;
import com.dbn.common.routine.ThrowableRunnable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class ThreadMonitor {
    private static final Map<ThreadProperty, AtomicInteger> PROCESS_COUNTERS = new ConcurrentHashMap<>();

    @Nullable
    public static Project getProject() {
        return ThreadInfo.current().getProject();
    }

    public static <E extends Throwable> void surround(
            @Nullable Project project,
            @Nullable ThreadProperty property,
            ThrowableRunnable<E> runnable) throws E {

        surround(project, property, () -> {
            runnable.run();
            return null;
        });
    }

    public static <T, E extends Throwable> T surround(
            @Nullable Project project,
            @Nullable ThreadProperty property,
            ThrowableCallable<T, E> callable) throws E {
        ThreadInfo threadInfo = ThreadInfo.current();
        Project originalProject = threadInfo.getProject();

        try {
            if (property != null) threadInfo.set(property, true);
            threadInfo.setProject(project);
            return callable.call();
        } finally {
            if (property != null) threadInfo.set(property, false);
            threadInfo.setProject(originalProject);
        }
    }


    public static <E extends Throwable> void surround(
            @Nullable Project project,
            @Nullable ThreadInfo invoker,
            @Nullable ThreadProperty property,
            ThrowableRunnable<E> runnable) throws E {

        surround(project, invoker, property, () -> {
            runnable.run();
            return null;
        });
    }

    public static <T, E extends Throwable> T surround(
            @Nullable Project project,
            @Nullable ThreadInfo invoker,
            @Nullable ThreadProperty property,
            ThrowableCallable<T, E> callable) throws E {

        ThreadInfo threadInfo = ThreadInfo.current();
        Project originalProject = threadInfo.getProject();

        boolean originalProperty = false;
        AtomicInteger processCounter = null;

        if (property != null) {
            originalProperty = threadInfo.is(property);
            processCounter = getProcessCounter(property);
        }

        try {
            threadInfo.merge(invoker);
            if (property != null) {
                processCounter.incrementAndGet();
                threadInfo.set(property, true);
            }

            threadInfo.setProject(project);
            threadInfo.setInvoker(invoker);
            return callable.call();

        } finally {
            if (property != null)  {
                processCounter.decrementAndGet();
                threadInfo.set(property, originalProperty);
            }
            threadInfo.unmerge(invoker);

            threadInfo.setProject(originalProject);
            threadInfo.setInvoker(null);
        }
    }

    public static boolean isTimeoutProcess() {
        return ThreadInfo.current().is(ThreadProperty.TIMEOUT);
    }

    public static boolean isBackgroundProcess() {
        return ThreadInfo.current().is(ThreadProperty.BACKGROUND);
    }

    public static boolean isProgressProcess() {
        return ThreadInfo.current().is(ThreadProperty.PROGRESS) || ProgressMonitor.isProgress();
    }

    public static boolean isModalProcess() {
        return ThreadInfo.current().is(ThreadProperty.MODAL) || ProgressMonitor.isProgressModal();
    }

    public static boolean isDisposerProcess() {
        return ThreadInfo.current().is(ThreadProperty.DISPOSER);
    }

    public static boolean isDispatchThread() {
        Application application = ApplicationManager.getApplication();
        return application != null && application.isDispatchThread();
    }

    public static boolean isReadActionThread() {
        Application application = ApplicationManager.getApplication();
        return application != null && application.isReadAccessAllowed();
    }

    public static boolean isWriteActionThread() {
        Application application = ApplicationManager.getApplication();
        return application != null && application.isWriteAccessAllowed();
    }

    public static boolean isTimeSensitiveThread() {
        if (isDispatchThread()) return true;
        if (isWriteActionThread()) return true;
        if (isReadActionThread()) return true;

        if (isBackgroundProcess()) return false;
        if (isTimeoutProcess()) return false;
        if (isProgressProcess()) return isModalProcess();

        return true;
    }

    public static int getProcessCount(ThreadProperty property) {
        return getProcessCounter(property).intValue();
    }

    private static AtomicInteger getProcessCounter(ThreadProperty property) {
        return PROCESS_COUNTERS.computeIfAbsent(property, p -> new AtomicInteger(0));
    }

    public static <E extends Throwable> void wrap(@NotNull ThreadProperty threadProperty, ThrowableRunnable<E> runnable) throws E {
        ThreadInfo threadInfo = ThreadInfo.current();
        boolean original = threadInfo.is(threadProperty);
        try {
            threadInfo.set(threadProperty, true);
            runnable.run();
        }
        finally {
            threadInfo.set(threadProperty, original);
        }
    }

    public static <R, E extends Throwable> R wrap(@NotNull ThreadProperty threadProperty, ThrowableCallable<R, E> callable) throws E {
        ThreadInfo threadInfo = ThreadInfo.current();
        boolean original = threadInfo.is(threadProperty);
        try {
            threadInfo.set(threadProperty, true);
            return callable.call();
        }
        finally {
            threadInfo.set(threadProperty, original);
        }
    }
}
