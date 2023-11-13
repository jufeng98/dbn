package com.dbn.common.thread;

import com.dbn.common.util.UUIDs;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Slf4j
public class PooledThread extends Thread{
    private static final ThreadGroup GROUP = new ThreadGroup("DBN");
    private static final Map<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();
    private Future<?> currentTask;
    private String currentTaskId;

    public PooledThread(String name, Runnable runnable) {
        super(GROUP, runnable, indexedName(name));
    }

    private static String indexedName(String name) {
        AtomicInteger index = COUNTERS.computeIfAbsent(name, n -> new AtomicInteger(0));
        return name + " " + index.incrementAndGet();
    }

    @Nullable
    public static PooledThread current() {
        Thread thread = Thread.currentThread();
        if (thread instanceof PooledThread) return (PooledThread) thread;
        return null;
    }

    public static String enter(Future<?> future) {
        PooledThread pooledThread = current();
        if (pooledThread == null) return null;

        String taskId = UUIDs.compact();
        String currentTaskId = pooledThread.getCurrentTaskId();

        if (currentTaskId != null) log.error("Incomplete thread context");
        pooledThread.setCurrentTask(future);
        pooledThread.setCurrentTaskId(taskId);
        return taskId;
    }

    public static void exit(String taskId) {
        PooledThread pooledThread = current();
        if (pooledThread == null) return;

        String currentTaskId = pooledThread.getCurrentTaskId();

        if (!Objects.equals(currentTaskId, taskId)) log.error("Invalid thread context");
        pooledThread.setCurrentTask(null);
        pooledThread.setCurrentTaskId(null);
    }

    public void cancel() {
        if (currentTask != null) currentTask.cancel(true);
    }

}
