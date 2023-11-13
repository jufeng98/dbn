package com.dbn.database.interfaces.queue;

import com.dbn.common.count.Counter;
import com.dbn.common.count.Counters;

import static com.dbn.common.count.CounterType.*;

public class InterfaceCounters extends Counters {
    public int active() {
        return queued().get() + running().get();
    }

    public Counter running() {
        return get(RUNNING);
    }

    public Counter queued() {
        return get(QUEUED);
    }

    public Counter finished() {
        return get(FINISHED);
    }

    @Override
    public String toString() {
        return
            "queued=" + queued().get() + " " +
            "running=" + running().get() + " " +
            "finished=" + finished().get();
    }
}
