package com.dbn.database.interfaces.queue;

import com.dbn.common.thread.ThreadInfo;
import lombok.Getter;

@Getter
public class InterfaceTaskSource {
    private final long timestamp = System.currentTimeMillis();
    private final Thread thread = Thread.currentThread();
    private final ThreadInfo threadInfo = ThreadInfo.copy();

    private final boolean waiting; // the source is waiting for the response

    public InterfaceTaskSource(boolean waiting) {
        this.waiting = waiting;
    }
}
