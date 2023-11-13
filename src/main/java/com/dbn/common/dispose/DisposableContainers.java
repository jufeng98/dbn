package com.dbn.common.dispose;

import com.intellij.openapi.Disposable;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@UtilityClass
public final class DisposableContainers {

    public static <T extends Disposable> List<T> list(Disposable parent) {
        return new DisposableList<>(parent);
    }

    public static <T extends Disposable> List<T> concurrentList(Disposable parent) {
        return new DisposableConcurrentList<>(parent);
    }

    public static <K, V extends Disposable> Map<K, V> map(Disposable parent) {
        return new DisposableMap<>(parent);
    }

    private static class DisposableList<T extends Disposable> extends ArrayList<T> implements Disposable{
        public DisposableList(@NotNull Disposable parent) {
            Disposer.register(parent, this);
        }

        @Override
        public void dispose() {
            BackgroundDisposer.queue(() -> Disposer.disposeCollection(this));
        }

        @Override
        public T remove(int index) {
            T removed = super.remove(index);
            Disposer.dispose(removed);
            return removed;
        }

        @Override
        public boolean remove(Object o) {
            boolean removed = super.remove(o);
            if (removed && o instanceof Disposable) {
                Disposable disposable = (Disposable) o;
                Disposer.dispose(disposable);
            }
            return removed;
        }
    }

    private static class DisposableConcurrentList<T extends Disposable> extends CopyOnWriteArrayList<T> implements Disposable{
        public DisposableConcurrentList(@NotNull Disposable parent) {
            Disposer.register(parent, this);
        }

        @Override
        public void dispose() {
            BackgroundDisposer.queue(() -> Disposer.disposeCollection(this));
        }

        @Override
        public T remove(int index) {
            T removed = super.remove(index);
            Disposer.dispose(removed);
            return removed;
        }

        @Override
        public boolean remove(Object o) {
            boolean removed = super.remove(o);
            if (removed && o instanceof Disposable) {
                Disposable disposable = (Disposable) o;
                Disposer.dispose(disposable);
            }
            return removed;
        }
    }


    private static class DisposableMap<K, V extends Disposable> extends HashMap<K, V> implements Disposable{
        public DisposableMap(@NotNull Disposable parent) {
            Disposer.register(parent, this);
        }

        @Override
        public void dispose() {
            Disposer.disposeMap(this);
        }

        @Override
        public boolean remove(Object key, Object value) {
            boolean removed = super.remove(key, value);
            if (removed && value instanceof Disposable) {
                Disposable disposable = (Disposable) value;
                Disposer.dispose(disposable);
            }
            return removed;
        }
    }
}
