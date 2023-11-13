package com.dbn.common.index;

import gnu.trove.TIntObjectHashMap;

public class IndexRegistry<T extends Indexable> {
    private final TIntObjectHashMap<T> INDEX = new TIntObjectHashMap<>();

    public void add(T element) {
        INDEX.put(element.index(), element);
    }

    public T get(int index) {
        return INDEX.get(index);
    }

    public int size() {
        return INDEX.size();
    }
}
