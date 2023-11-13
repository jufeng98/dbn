package com.dbn.common.util;

import com.dbn.common.routine.ThrowableCallable;
import com.dbn.common.routine.ThrowableRunnable;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public final class Measured {

    @SneakyThrows
    public static void run(String identifier, ThrowableRunnable<Throwable> runnable) {
        logStart(identifier);
        long start = System.currentTimeMillis();
        try {
            runnable.run();
        } finally {
            logEnd(identifier, start);
        }
    }

    @SneakyThrows
    public static <T> T call(String identifier, ThrowableCallable<T, Throwable> callable) {
        logStart(identifier);
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            logEnd(identifier, start);
        }
    }

    private static void logStart(String identifier) {
        log.info("[DBN] Started " + identifier);
    }

    private static void logEnd(String identifier, long start) {
        log.info("[DBN] Done " + identifier + " - " + (System.currentTimeMillis() - start) + "ms");
    }
}
