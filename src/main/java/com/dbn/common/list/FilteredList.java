package com.dbn.common.list;

import com.dbn.common.filter.Filter;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public interface FilteredList<T> extends List<T> {
    List<T> getBase();

    @Nullable
    Filter<T> getFilter();

    // update methods should not be affected by filtering
    @Override
    void sort(Comparator<? super T> comparator);

    void trimToSize();

    void setFilter(Filter<T> filter);

    static <T> StatefulFilteredList<T> stateful(Filter<T> filter) {
        return new StatefulFilteredList<>(filter);
    }

    static <T> StatefulFilteredList<T> stateful(Filter<T> filter, List<T> base) {
        return new StatefulFilteredList<>(filter, base);
    }

    static <T> StatelessFilteredList<T> stateless(Filter<T> filter) {
        return new StatelessFilteredList<>(filter);
    }

    static <T> StatelessFilteredList<T> stateless(Filter<T> filter, List<T> base) {
        return new StatelessFilteredList<>(filter, base);
    }

    static <T> List<T> unwrap(List<T> list) {
        if (list instanceof FilteredList) {
            FilteredList<T> filteredList = (FilteredList<T>) list;
            return filteredList.getBase();
        }
        return list;
    }
}
