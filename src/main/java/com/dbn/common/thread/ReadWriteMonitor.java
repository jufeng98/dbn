package com.dbn.common.thread;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ReadWriteMonitor {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @SneakyThrows
    public <T> T read(Callable<T> callable) {
        Lock readLock = this.lock.readLock();
        try {
            readLock.lock();
            return callable.call();
        } finally {
            readLock.unlock();
        }
    }

    public void write(Runnable runnable) {
        Lock writeLock = this.lock.writeLock();
        try {
            writeLock.lock();
            runnable.run();
        } finally {
            writeLock.unlock();
        }
    }
}
