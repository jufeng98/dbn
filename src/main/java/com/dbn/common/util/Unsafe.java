package com.dbn.common.util;

import com.dbn.common.routine.ParametricCallable;
import com.dbn.common.routine.ParametricRunnable;
import com.dbn.common.routine.ThrowableCallable;
import com.dbn.common.routine.ThrowableRunnable;
import com.intellij.openapi.progress.ProcessCanceledException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
@UtilityClass
public final class Unsafe {

    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static boolean silent(ThrowableRunnable<Throwable> runnable) {
        try {
            runnable.run();
            return true;
        } catch (Throwable e) {
            conditionallyLog(e);
            return false;
        }
    }

    public static <P> boolean silent(P param, ParametricRunnable<P, Throwable> runnable) {
        try {
            runnable.run(param);
            return true;
        } catch (Throwable e) {
            conditionallyLog(e);
            return false;
        }
    }

    public static <R> R silent(R defaultValue, ThrowableCallable<R, Throwable> callable) {
        try {
            return callable.call();
        } catch (Throwable e) {
            conditionallyLog(e);
            return defaultValue;
        }
    }

    public static <P, R> R silent(R defaultValue, P param, ParametricCallable<P, R, Throwable> callable) {
        try {
            return callable.call(param);
        } catch (Throwable e) {
            conditionallyLog(e);
            return defaultValue;
        }
    }

    public static void warned(ThrowableRunnable<Throwable> runnable) {
        try {
            runnable.run();
        } catch (ProcessCanceledException e) {
            conditionallyLog(e);
        } catch (Throwable e) {
            conditionallyLog(e);
            String message = e.getMessage();
            log.warn(message == null ? e.getClass().getSimpleName() : message);
        }
    }

    public static <T> T warned(T defaultValue, ThrowableCallable<T, Throwable> callable) {
        try {
            return callable.call();
        } catch (ProcessCanceledException e) {
            conditionallyLog(e);
        } catch (Throwable e) {
            conditionallyLog(e);
            String message = e.getMessage();
            log.warn(message == null ? e.getClass().getSimpleName() : message);
        }
        return defaultValue;
    }

}
