package com.dbn.common.thread;

import com.dbn.common.routine.ParametricCallable;
import com.dbn.common.routine.ParametricRunnable;
import com.dbn.common.util.Unsafe;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@UtilityClass
public class Synchronized {
	static final Map<Object, SyncObject> LOCKS = new ConcurrentHashMap<>(100);

	public static <O, E extends Throwable> void on(O owner, ParametricRunnable<O, E> runnable) throws E{
		SyncObject<O> lock = acquire(owner);
		try {
			lock.execute(owner, runnable);
		} finally {
			release(owner, lock);
		}
	}

	public static <O, R, E extends Throwable> R on(O owner, ParametricCallable<O, R, E> callable) throws E{
		SyncObject<R> lock = acquire(owner);
		try {
			return lock.execute(owner, callable);
		} finally {
			release(owner, lock);
		}
	}

	private static synchronized <R> SyncObject<R> acquire(Object owner) {
		return Unsafe.cast(LOCKS.computeIfAbsent(owner, o -> new SyncObject()));
	}

	private synchronized static void release(Object owner, SyncObject<?> lock) {
		if (lock.isFree()) LOCKS.remove(owner);
	}

	private static class SyncObject<R> {
		private final Lock lock = new ReentrantLock();
		private final AtomicInteger invokers = new AtomicInteger();

		public <O, E extends Throwable> R execute(O owner, ParametricCallable<O, R, E> callable) throws E{
			invokers.incrementAndGet();
			lock.lock();
			try {
				return callable.call(owner);
			} finally {
				invokers.decrementAndGet();
				lock.unlock();
			}
		}

		public <O, E extends Throwable> void execute(O owner, ParametricRunnable<O, E> runnable) throws E{
			invokers.incrementAndGet();
			lock.lock();
			try {
				runnable.run(owner);
			} finally {
				invokers.decrementAndGet();
				lock.unlock();
			}
		}

		public boolean isFree() {
			return invokers.get() == 0;
		}
	}
}

