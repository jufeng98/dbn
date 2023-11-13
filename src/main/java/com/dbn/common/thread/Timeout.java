package com.dbn.common.thread;

import com.dbn.common.exception.Exceptions;
import com.dbn.common.routine.ThrowableCallable;
import com.dbn.common.routine.ThrowableRunnable;
import com.dbn.common.util.Commons;
import com.dbn.common.util.TimeUtil;
import com.dbn.diagnostics.Diagnostics;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
@UtilityClass
public final class Timeout {
    private static final Object lock = new Object();

    @SneakyThrows
    public static <T> T call(String identifier, int seconds, T defaultValue, boolean daemon, ThrowableCallable<T, Throwable> callable) {
        long start = System.currentTimeMillis();
        try {
            Threads.delay(lock);
            seconds = Diagnostics.timeoutAdjustment(seconds);
            ThreadInfo invoker = ThreadInfo.copy();
            ExecutorService executorService = Threads.timeoutExecutor(daemon);

            AtomicReference<Future<T>> future = new AtomicReference<>();
            AtomicReference<Throwable> exception = new AtomicReference<>();

            future.set(executorService.submit(() -> {
                String taskId = PooledThread.enter(future.get());
                try {
                    return ThreadMonitor.surround(
                            invoker.getProject(),
                            invoker,
                            ThreadProperty.TIMEOUT,
                            callable);
                } catch (Throwable e) {
                    conditionallyLog(e);
                    exception.set(e);
                    return null;
                } finally {
                    PooledThread.exit(taskId);
                }
            }));


            T result = waitFor(future.get(), seconds, TimeUnit.SECONDS);
            if (exception.get() != null) {
                throw exception.get();
            }
            return result;
        } catch (TimeoutException | InterruptedException | RejectedExecutionException e) {
            conditionallyLog(e);
            String message = Commons.nvl(e.getMessage(), e.getClass().getSimpleName());
            log.warn("{} - Operation timed out after {}s (timeout = {}s). Defaulting to {}. Cause: {}", identifier, TimeUtil.secondsSince(start), seconds, defaultValue, message);
        } catch (ExecutionException e) {
            conditionallyLog(e);
            log.warn("{} - Operation failed after {}s (timeout = {}s). Defaulting to {}", identifier, TimeUtil.secondsSince(start), seconds, defaultValue, Exceptions.causeOf(e));
            throw e.getCause();
        } catch (Throwable e) {
            conditionallyLog(e);
            throw e;
        }
        return defaultValue;
    }

    @SneakyThrows
    public static void run(int seconds, boolean daemon, ThrowableRunnable<Throwable> runnable) {
        long start = System.currentTimeMillis();
        try {
            Threads.delay(lock);
            seconds = Diagnostics.timeoutAdjustment(seconds);
            ThreadInfo invoker = ThreadInfo.copy();
            ExecutorService executorService = Threads.timeoutExecutor(daemon);
            AtomicReference<Future<?>> future = new AtomicReference<>();
            AtomicReference<Throwable> exception = new AtomicReference<>();

            future.set(executorService.submit(() -> {
                String taskId = PooledThread.enter(future.get());
                try {
                    ThreadMonitor.surround(
                            invoker.getProject(),
                            invoker,
                            ThreadProperty.TIMEOUT,
                            runnable);
                } catch (Throwable e) {
                    conditionallyLog(e);
                    exception.set(e);
                } finally {
                    PooledThread.exit(taskId);
                }
            }));
            waitFor(future.get(), seconds, TimeUnit.SECONDS);
            if (exception.get() != null) {
                throw exception.get();
            }

        } catch (TimeoutException | InterruptedException | RejectedExecutionException e) {
            conditionallyLog(e);
            String message = Commons.nvl(e.getMessage(), e.getClass().getSimpleName());
            log.warn("Operation timed out after {}s (timeout = {}s). Cause: {}", TimeUtil.secondsSince(start), seconds, message);
        } catch (ExecutionException e) {
            conditionallyLog(e);
            log.warn("Operation failed after {}s (timeout = {}s)", TimeUtil.secondsSince(start), seconds, Exceptions.causeOf(e));
            throw e.getCause();
        } catch (Throwable e) {
            conditionallyLog(e);
            throw e;
        }
    }

    public static <T> T waitFor(Future<T> future, long time, TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
        try {
            return future.get(time, timeUnit);
        } catch (TimeoutException | InterruptedException e) {
            conditionallyLog(e);
            future.cancel(true);
            throw e;
        }
    }

}
