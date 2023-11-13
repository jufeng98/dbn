package com.dbn.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Future;

@Slf4j
@UtilityClass
public class Current {
    private static final ThreadLocal<Future> currentTask = new ThreadLocal<>();
    private static final ThreadLocal<String> currentTaskId = new ThreadLocal<>();

    public static String start() {
        return start(null);
    }

    public static String start(Future<?> task) {
        if (Current.currentTaskId.get() != null) {
            log.error("Incomplete thread context");
        }

        String taskId = UUIDs.compact();
        currentTaskId.set(taskId);
        currentTask.set(task);
        return taskId;
    }

    public static void end(String taskId) {
        if (!Objects.equals(currentTaskId.get(), taskId)) {
            log.error("Invalid thread context");
        }
        currentTaskId.remove();
        currentTask.remove();
    }

    public static String currentTaskId() {
        return currentTaskId.get();
    }

    public static <T> Future<T> currentTask() {
        return Unsafe.cast(currentTask.get());
    }

    public void cancel() {
        Future future = currentTask.get();
        if (future != null) future.cancel(true);
    }

}
