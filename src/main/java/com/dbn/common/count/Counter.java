package com.dbn.common.count;

import com.dbn.common.ui.util.Listeners;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public final class Counter {
    @Getter
    private final CounterType type;
    private final Listeners<CounterListener> listeners = Listeners.create();
    private final AtomicInteger count = new AtomicInteger(0);

    public Counter(CounterType type) {
        this.type = type;
    }

    public int increment() {
        int value = count.incrementAndGet();
        listeners.notify(l -> l.when(value));
        return value;
    }

    public int decrement() {
        int value = count.updateAndGet(i -> i > 0 ? i - 1 : i);
        listeners.notify(l -> l.when(value));
        return value;
    }

    public int get() {
        return count.get();
    }

    public void set(int count) {
        this.count.set(count);
    }

    public int max(int count) {
        return this.count.updateAndGet(i -> Math.max(i, count));
    }

    public int min(int count) {
        return this.count.updateAndGet(i -> Math.min(i, count));
    }

    public void reset() {
        set(0);
    }

    @Override
    public String toString() {
        return count.toString();
    }

    public void addListener(CounterListener listener) {
        listeners.add(listener);
    }
}
