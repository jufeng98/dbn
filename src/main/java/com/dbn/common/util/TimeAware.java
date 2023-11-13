package com.dbn.common.util;

import java.util.concurrent.TimeUnit;

public interface TimeAware {
    long getTimestamp();

    default boolean isOlderThan(long duration, TimeUnit timeUnit) {
        return System.currentTimeMillis() - timeUnit.toMillis(duration) > getTimestamp();
    }
}
