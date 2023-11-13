package com.dbn.connection.jdbc;

import com.dbn.common.property.Property;
import com.dbn.common.property.PropertyHolder;
import com.dbn.common.ref.WeakRef;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public abstract class IncrementalStatusAdapter<T extends PropertyHolder<P>, P extends Property.IntBase> {
    private final P status;
    private final WeakRef<T> resource;
    private final AtomicInteger count = new AtomicInteger();

    public IncrementalStatusAdapter(T resource, P status) {
        this.status = status;
        this.resource = WeakRef.of(resource);
    }

    public final boolean set(boolean value) {
        int current = value ?
                count.incrementAndGet() :
                count.decrementAndGet();

        boolean changed = setInner(status, current > 0);
        if (changed) statusChanged();
        return changed;
    }

    public T getResource() {
        return resource.ensure();
    }

    public P getStatus() {
        return status;
    }

    public AtomicInteger getCount() {
        return count;
    }

    protected abstract void statusChanged();

    protected abstract boolean setInner(P status, boolean value);
}
