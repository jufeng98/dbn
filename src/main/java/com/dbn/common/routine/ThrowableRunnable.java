package com.dbn.common.routine;

@FunctionalInterface
public interface ThrowableRunnable<E extends Throwable> {
    void run() throws E;

    static <E extends Throwable> ThrowableRunnable<E> from(ThrowableCallable<?, E> callable) {
        return () -> callable.call();
    }
}
