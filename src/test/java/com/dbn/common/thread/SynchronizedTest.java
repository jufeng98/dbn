package com.dbn.common.thread;

import com.dbn.common.Pair;
import com.dbn.common.thread.Synchronized;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class SynchronizedTest {
    private final Random random = new Random();
    public static final int ITERATIONS = 100;
    public static final int THREADS = 10;
    public static final int KEYS = 20;

    @Test
    public void sequencedExecutionTest() throws Exception {
        long begin = System.currentTimeMillis();
        Map<String, List<Pair<Long, Long>>> callLogs = new ConcurrentHashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < ITERATIONS; i++) {
            for (int c = 0; c < KEYS; c++) {
                String key = "KEY" + c;

                executorService.submit(() -> {
                    Synchronized.on(key, o -> {
                        long start = System.currentTimeMillis();
                        int delay = random.nextInt(200);
                        long delayMillis = TimeUnit.MILLISECONDS.toNanos(delay);
                        LockSupport.parkNanos(new Object(), delayMillis);
                        long end = System.currentTimeMillis();

                        List<Pair<Long, Long>> callLog = callLogs.computeIfAbsent(key, k -> new ArrayList<>());
                        callLog.add(Pair.of(start, end));
                        System.out.println(key + ": " + (start % 10000) + " -> " + (end % 10000));
                    });
                });

            }
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        for (int c = 0; c < KEYS; c++) {
            String key = "KEY" + c;

            Pair<Long, Long> previous = Pair.of(begin, begin);
            List<Pair<Long, Long>> callLog = callLogs.get(key);
            for (Pair<Long, Long> current : callLog) {
                Long previousEnd = previous.second();
                Long currentStart = current.first();

                System.out.println(key + ": " + (currentStart - previousEnd));
                if (currentStart >= previousEnd)
                    successCount.incrementAndGet(); else
                    failureCount.incrementAndGet();
                previous = current;
            }
        }

        Assert.assertEquals(0, failureCount.get());
        Assert.assertTrue(Synchronized.LOCKS.isEmpty());
    }
}