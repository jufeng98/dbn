package com.dbn.database;

import com.dbn.common.util.TimeUtil;

import java.sql.SQLException;

public class DatabaseActivityTrace {
    private boolean supported = true;
    private int failedAttempts;
    private long lastAttempt;
    private SQLException exception;

    public void init() {
        lastAttempt = System.currentTimeMillis();
        exception = null;
    }

    public void release() {
        if (exception == null) {
            failedAttempts = 0;
        }
    }

    public void fail(SQLException exception, boolean invalidate) {
        this.exception = exception;
        this.failedAttempts++;
        if (invalidate) {
            this.supported = false;
        }
    }

    public boolean canExecute(boolean hasFallback) {
        // do not allow more then three calls
        long now = System.currentTimeMillis();

        boolean allowRetrial = supported && !hasFallback && TimeUtil.isOlderThan(lastAttempt, 5000);
        if (failedAttempts < 3 || allowRetrial) {
            if (allowRetrial) {
                lastAttempt = now;
                failedAttempts = 0;
            }
            return true;
        }
        return false;
    }

    public SQLException getException() {
        return exception;
    }
}
