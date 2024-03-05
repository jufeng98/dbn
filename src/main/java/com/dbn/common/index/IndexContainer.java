package com.dbn.common.index;

import com.dbn.common.util.Compactable;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class IndexContainer<T extends Indexable> implements Compactable {
    private final TIntHashSet INDEX = new TIntHashSet();

    public void add(T element) {
        INDEX.add(element.index());
    }

    public boolean isEmpty() {
        return INDEX.isEmpty();
    }

    public boolean contains(T indexable) {
        try {
            return INDEX.contains(indexable.index());
        } catch (Throwable e) {
            // TODO workaround - IOOBE, NPE happens in parser lookup caches (probably due to latent background initialization)
            conditionallyLog(e);
            return false;
        }
    }

    public Set<T> elements(Function<Integer, T> resolver) {
        if (INDEX.isEmpty()) {
            return Collections.emptySet();
        } else {
            Set<T> elements = new HashSet<>(INDEX.size());
            TIntIterator iterator = INDEX.iterator();
            while (iterator.hasNext()) {
                int next = iterator.next();
                T element = resolver.apply(next);
                if (element != null) {
                    elements.add(element);
                }
            }
            return elements;
        }
    }

    @Override
    public void compact() {
        INDEX.trimToSize();
    }

    public void addAll(Collection<T> elements) {
        for (T element : elements) {
            INDEX.add(element.index());
        }
    }
}
