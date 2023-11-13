package com.dbn.database.interfaces.queue;

import com.dbn.common.count.Counter;
import com.dbn.common.count.CounterType;
import com.dbn.common.count.Counters;

public class InterfaceThreadMonitor {
    private static final Counters progressThreads = new Counters();
    private static final Counters backgroundThreads = new Counters();

    public static int getRunningThreadCount(boolean progress) {
        return getCounter(CounterType.RUNNING, progress).get();
    }

    public static Counter getCounter(CounterType counterType, boolean progress) {
        return progress ?
                progressThreads.get(counterType) :
                backgroundThreads.get(counterType);
    }

    public static void start(boolean progress) {
        int count = getCounter(CounterType.RUNNING, progress).increment();
        getCounter(CounterType.PEAK, progress).max(count);
    }

    public static void finish(boolean progress) {
        getCounter(CounterType.RUNNING, progress).decrement();
        getCounter(CounterType.FINISHED, progress).increment();
    }
}
