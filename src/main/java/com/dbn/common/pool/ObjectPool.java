package com.dbn.common.pool;

import java.util.concurrent.TimeUnit;

public interface ObjectPool<T, E extends Throwable> {
    /**
     * Acquire an object from the pool
     *
     * @param timeout time to give up
     * @param timeUnit the unit of time to give op
     * @return an object from the pool
     * @throws E when the pool reached limits or failed to initialize the object
     */
    T acquire(long timeout, TimeUnit timeUnit) throws E;

    /**
     * Release the object back to the pool
     * The object will be made available for the next {@link #acquire(long, TimeUnit)} operation
     *
     * @param object the object to be released
     * @return the released object
     */
    T release(T object);

    /**
     * Drop an object from the pool
     *
     * @param object the object to be removed
     * @return the removed object
     */
    T drop(T object);

    int size();

    int maxSize();

    int peakSize();

    default boolean isEmpty() {
        return size() == 0;
    }
}
