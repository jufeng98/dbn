package com.dbn.common.tracing;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class ProcessingSummary {
    private final String identifier;
    private final AtomicLong invocations = new AtomicLong(0);
    private final AtomicLong failures = new AtomicLong(0);
    private final AtomicLong processingTime = new AtomicLong(0);
    private final Set<String> failureMessages = new HashSet<>();

    public ProcessingSummary(String identifier) {
        this.identifier = identifier;
    }

    public void success(long executionTime) {
        invocations.incrementAndGet();
        processingTime.addAndGet(executionTime);
    }

    public void failure(long executionTime, String message) {
        failures.incrementAndGet();
        invocations.incrementAndGet();
        processingTime.addAndGet(executionTime);
        failureMessages.add(message);
    }

    public String identifier() {
        return identifier;
    }

    public long invocations() {
        return invocations.get();
    }

    public long getFailures() {
        return failures.get();
    }

    public long totalProcessingTime() {
        return processingTime.get();
    }

    public long averageProcessingTime() {
        long invocations = invocations();
        if (invocations == 0) {
            return 0;
        }
        long totalProcessingTime = totalProcessingTime();
        return totalProcessingTime / invocations;
    }
}
