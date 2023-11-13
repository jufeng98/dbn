package com.dbn.common.util;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class ChangeTimestamp {
    private final Timestamp value;
    private final long captureTime;

    public ChangeTimestamp() {
        this.captureTime = System.currentTimeMillis();
        this.value = new Timestamp(captureTime);
    }
    public ChangeTimestamp(@NotNull Timestamp value) {
        this.value = value;
        this.captureTime = System.currentTimeMillis();
    }

    public static ChangeTimestamp of(@NotNull Timestamp value) {
        return new ChangeTimestamp(value);
    }

    public static ChangeTimestamp now() {
        return new ChangeTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    @NotNull
    public Timestamp value() {
        return value;
    }

    public boolean isDirty() {
        return TimeUtil.isOlderThan(captureTime, 30 * TimeUtil.Millis.ONE_SECOND);
    }

    public boolean isOlderThan(ChangeTimestamp changeTimestampCheck) {
        return value.before(changeTimestampCheck.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
