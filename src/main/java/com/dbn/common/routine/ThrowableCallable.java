package com.dbn.common.routine;

@FunctionalInterface
public interface ThrowableCallable<R, E extends Throwable> {
    R call() throws E;

    static <E extends Throwable> ThrowableCallable<?, E> from(ThrowableRunnable<E> runnable) {
        return (ThrowableCallable<Object, E>) () -> {
            runnable.run();
            return null;
        };
    }
}
