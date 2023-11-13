package com.dbn.common.routine;

@FunctionalInterface
public interface ParametricRunnable<P, E extends Throwable> {
    void run(P param) throws E;
}
