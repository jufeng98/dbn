package com.dbn.common.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@EqualsAndHashCode
public class CompoundFilter<T> implements Filter<T>{
    private final List<Filter<T>> filters;

    private CompoundFilter(List<Filter<T>> filters) {
        this.filters = filters;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Filter<T> of(Filter<T> ... filters) {
        return new CompoundFilter<>(Arrays.asList(filters));
    }

    @Override
    public final boolean accepts(T object) {
        List<Filter<T>> filters = getFilters();
        for (Filter<T> filter : filters) {
            if (filter != null && !filter.accepts(object)) {
                return false;
            }
        }

        return true;
    }
}
