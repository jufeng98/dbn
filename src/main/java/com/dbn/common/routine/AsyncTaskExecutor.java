package com.dbn.common.routine;

import com.dbn.common.util.Unsafe;
import com.intellij.util.containers.ContainerUtil;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class AsyncTaskExecutor {
    private final ExecutorService executor;
    private final Set<Future<?>> tasks = ContainerUtil.newConcurrentSet();
    private @Getter boolean finished;

    private final long timeout;
    private final TimeUnit timeUnit;

    public AsyncTaskExecutor(ExecutorService executor, long timeout, TimeUnit timeUnit) {
        this.executor = executor;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void submit(Runnable runnable) {
        tasks.add(executor.submit(() -> Unsafe.warned(() -> runnable.run())));
    }

    public void awaitCompletion() {
        Unsafe.warned(() -> {
            Set<Future<?>> tasks = new HashSet<>(this.tasks);
            this.tasks.clear();
            List<Future<Object>> futures = executor.invokeAll(
                    tasks.stream().
                            filter(future -> !future.isDone()).
                            map(future -> (Callable<Object>) () -> future.get()).
                            collect(Collectors.toList()),
                    timeout,
                    timeUnit);


            for (Future<Object> future : futures) {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
        });
        finished = true;
    }
}
