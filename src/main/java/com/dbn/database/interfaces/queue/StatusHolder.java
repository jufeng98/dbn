package com.dbn.database.interfaces.queue;

import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StatusHolder<T extends InterfaceTaskStatus> {
    private final Stack<T> history = new Stack<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private T status;

    public StatusHolder(T initial) {
        this.status = initial;
    }

    public Stack<T> getHistory() {
        return history;
    }

    boolean change(T status) {
        Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();

            if (!status.isAfter(this.status)) {
                // violation
                return false;
            }

            this.history.push(this.status);
            this.status = status;
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    public T get() {
        Lock readLock = lock.readLock();
        try {
            readLock.lock();
            return status;
        } finally {
            readLock.unlock();
        }
    }

    public boolean is(T status) {
        return get() == status;
    }

    public boolean isBefore(T status) {
        return this.status.compareTo(status) < 0;
    }

    @Override
    public String toString() {
        return status.toString();
    }
}
