package com.dbn.common.count;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dbn.common.util.Strings.cachedLowerCase;

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
            builder.append(cachedLowerCase(counterType.name()));
            builder.append("=");
            builder.append(counter.get());
        }

        return builder.toString();
    }
}
