package com.dbn.diagnostics.data;

import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class DiagnosticEntryBase<T> implements DiagnosticEntry<T> {
    private final T identifier;
    private final String qualifier = DEFAULT_QUALIFIER;
    private final AtomicLong invocations = new AtomicLong();
    private final AtomicLong failures = new AtomicLong();
    private final AtomicLong timeouts = new AtomicLong();

    private final AtomicLong total = new AtomicLong();
    private final AtomicLong best = new AtomicLong();
    private final AtomicLong worst = new AtomicLong();

    public DiagnosticEntryBase(T identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public DiagnosticEntry<T> getDetail(String qualifier) {
        if (!Objects.equals(this.qualifier, qualifier))
            throw new IllegalArgumentException("Only supported for composites");

        return this;
    }

    @Override
    public long getInvocations() {
        return invocations.get();
    }

    @Override
    public long getFailures() {
        return failures.get();
    }

    @Override
    public long getTimeouts() {
        return timeouts.get();
    }

    @Override
    public long getTotal() {
        return total.get();
    }

    @Override
    public long getAverage() {
        long totalTime = getTotal();
        long invocations = getInvocations();
        return invocations == 0 ? 0 : totalTime / invocations;
    }

    @Override
    public long getBest() {
        return best.get();
    }

    @Override
    public long getWorst() {
        return worst.get();
    }

    @Override
    public void log(boolean failure, boolean timeout, long value) {
        invocations.incrementAndGet();
        if (failure) {
            failures.incrementAndGet();
        }
        if (timeout) {
            timeouts.incrementAndGet();
        }

        if (best.get() == 0 || best.get() > value) {
            best.set(value);
        }
        if (worst.get() == 0 || worst.get() < value) {
            worst.set(value);
        }

        total.addAndGet(value);
    }


}
