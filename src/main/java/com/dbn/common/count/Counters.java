package com.dbn.common.count;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Counters {
    private final Map<CounterType, Counter> counters = new ConcurrentHashMap<>();

    public Counter get(CounterType type) {
        return counters.computeIfAbsent(type, t -> new Counter(t));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (CounterType counterType : counters.keySet()) {
            Counter counter = counters.get(counterType);

            if (builder.length() > 0) builder.append(" ");
            builder.append(counterType.name().toLowerCase());
            builder.append("=");
            builder.append(counter.get());
        }

        return builder.toString();
    }
}
