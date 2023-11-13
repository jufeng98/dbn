package com.dbn.common.list;

import com.dbn.common.filter.Filter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class FilteredListBase<T> implements FilteredList<T> {
    protected List<T> base;
    protected Filter<T> filter;


    public FilteredListBase(Filter<T> filter, List<T> base) {
        this.base = initBase(base);
        this.filter = filter;
    }

    public FilteredListBase(Filter<T> filter) {
        this.base = initBase(null);
        this.filter = filter;
    }

    abstract List<T> initBase(List<T> source);

    @Nullable
    @Override
    public final Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }
}
