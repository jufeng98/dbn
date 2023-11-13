package com.dbn.common.list;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MostRecentStack<T> implements Iterable<T>{
    private List<T> values = new ArrayList<>();

    public MostRecentStack() {
    }

    public MostRecentStack(Iterable<T> values) {
        for (T value : values) {
            this.values.add(value);
        }
    }

    public void add(T value) {
        values.add(value);
    }

    public void stack(T value) {
        values.remove(value);
        values.add(0, value);
    }

    @Nullable
    public T get() {
        return values.size() > 0 ? values.get(0) : null;
    }

    public List<T> values(){
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }
}
