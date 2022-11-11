package com.dci.intellij.dbn.common.thread;

import com.dci.intellij.dbn.common.routine.ThrowableCallable;
import com.dci.intellij.dbn.common.routine.ThrowableRunnable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dci.intellij.dbn.common.thread.ThreadProperty.BACKGROUND;
import static com.dci.intellij.dbn.common.thread.ThreadProperty.DISPOSER;
import static com.dci.intellij.dbn.common.thread.ThreadProperty.PROGRESS;
import static com.dci.intellij.dbn.common.thread.ThreadProperty.TIMEOUT;

public class ThreadMonitor {
    private static final ThreadLocal<ThreadInfo> THREAD_PROPERTIES = new ThreadLocal<>();
    private static final Map<ThreadProperty, AtomicInteger> PROCESS_COUNTERS = new ConcurrentHashMap<>();

    public static ThreadInfo current() {
        ThreadInfo threadInfo = THREAD_PROPERTIES.get();
        if (threadInfo == null) {
            threadInfo = new ThreadInfo();
            THREAD_PROPERTIES.set(threadInfo);
        }
        return threadInfo;
    }



    public static <E extends Throwable> void run(
            @Nullable ThreadInfo invoker,
            @NotNull ThreadProperty threadProperty,
            ThrowableRunnable<E> runnable) throws E {

        ThreadInfo threadInfo = current();
        boolean originalProperty = threadInfo.is(threadProperty);
        AtomicInteger processCounter = getProcessCounter(threadProperty);
        try {
            processCounter.incrementAndGet();
            threadInfo.set(threadProperty, true);
            threadInfo.merge(invoker);
            runnable.run();
        } catch (ProcessCanceledException  ignore){
        } finally {
            threadInfo.set(threadProperty, originalProperty);
            processCounter.decrementAndGet();
            threadInfo.unmerge(invoker);
        }
    }

    public static <T, E extends Throwable> T call(
            @Nullable ThreadInfo invoker,
            @NotNull ThreadProperty threadProperty,
            T defaultValue,
            ThrowableCallable<T, E> callable) throws E{

        ThreadInfo threadInfo = current();
        boolean originalProperty = threadInfo.is(threadProperty);
        AtomicInteger processCounter = getProcessCounter(threadProperty);
        try {
            processCounter.incrementAndGet();
            threadInfo.set(threadProperty, true);
            threadInfo.merge(invoker);
            return callable.call();
        } catch (ProcessCanceledException e) {
            return defaultValue;
        } finally {
            threadInfo.set(threadProperty, originalProperty);
            threadInfo.unmerge(invoker);
            processCounter.decrementAndGet();
        }
    }

    public static boolean isTimeoutProcess() {
        return current().is(TIMEOUT);
    }

    public static boolean isProgressProcess() {
        return current().is(PROGRESS);
    }

    public static boolean isBackgroundProcess() {
        return current().is(BACKGROUND);
    }

    public static boolean isDisposerProcess() {
        return current().is(DISPOSER);
    }

    public static boolean isDispatchThread() {
        return ApplicationManager.getApplication().isDispatchThread();
    }

    public static int getProcessCount(ThreadProperty property) {
        return getProcessCounter(property).intValue();
    }

    private static AtomicInteger getProcessCounter(ThreadProperty property) {
        return PROCESS_COUNTERS.computeIfAbsent(property, p -> new AtomicInteger(0));
    }

    public static <E extends Throwable> void wrap(@NotNull ThreadProperty threadProperty, ThrowableRunnable<E> runnable) throws E {
        ThreadInfo threadInfo = ThreadMonitor.current();
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
        ThreadInfo threadInfo = ThreadMonitor.current();
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