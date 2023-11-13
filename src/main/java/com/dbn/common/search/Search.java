package com.dbn.common.search;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
public final class Search {

    public static <T> T binarySearch(@Nullable List<T> list, SearchAdapter<T> adapter) {
        if (list == null || list.isEmpty()) return null;

        return binarySearch(list, 0, list.size() - 1, adapter);
    }

    public static <T> T binarySearch(@Nullable List<T> list, int left, int right, SearchAdapter<T> adapter) {
        if (list == null || list.isEmpty()) return null;

        while (left <= right) {
            int mid = left + right >>> 1;
            T midVal = list.get(mid);
            int result = adapter.evaluate(midVal);
            if (result < 0) {
                left = mid + 1;
            } else if (result > 0){
                right = mid - 1;
            } else {
                return list.get(mid);
            }
        }
        return null;
    }

    public static <T> T binarySearch(@Nullable T[] array, SearchAdapter<T> adapter) {
        if (array == null || array.length == 0) return null;

        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + right >>> 1;
            T midVal = array[mid];
            int result = adapter.evaluate(midVal);
            if (result < 0) {
                left = mid + 1;
            } else if (result > 0){
                right = mid - 1;
            } else {
                return array[mid];
            }
        }
        return null;
    }


    public static <T> T linearSearch(List<T> list, SearchAdapter<T> adapter) {
        if (list == null || list.isEmpty()) return null;

        for (T element : list) {
            int result = adapter.evaluate(element);
            if (result >= 0) {
                if (result == 0) {
                    return element;
                }
            } else {
                return null;
            }

        }
        return null;
    }

    public static <T> T comboSearch(List<T> list, SearchAdapter<T> linear, SearchAdapter<T> binary) {
        if (list == null || list.isEmpty()) return null;

        int index = 0;

        while (index < list.size()) {
            T element = list.get(index);
            int result = linear.evaluate(element);
            if (result >= 0) {
                if (result == 0) {
                    return element;
                }
                index++;
            } else {
                break;
            }
        }
        return binarySearch(list, index, list.size() - 1, binary);
    }
}
