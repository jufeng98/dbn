package com.dbn.common.pool;

import com.dbn.common.count.Counter;
import com.dbn.common.count.CounterType;
import com.dbn.common.count.Counters;

public class ObjectPoolCounters extends Counters {
    public Counter peak() {
        return get(CounterType.PEAK);
    }

    public Counter waiting() {
        return get(CounterType.WAITING);
    }

    public Counter reserved() {
        return get(CounterType.RESERVED);
    }

    public Counter rejected() {
        return get(CounterType.REJECTED);
    }

    public Counter creating() {
        return get(CounterType.CREATING);
    }

    @Override
    public String toString() {
        return
            "peak=" + peak().get() + " " +
            "waiting=" + waiting().get() + " " +
            "reserved=" + reserved().get() + " " +
            "rejected=" + rejected().get() + " " +
            "creating=" + creating().get()
                ;
    }
}
