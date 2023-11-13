package com.dbn.object.common;

import com.dbn.common.search.SearchAdapter;
import com.dbn.object.DBType;

import java.util.function.Predicate;

public class DBObjectSearchAdapters {
    private DBObjectSearchAdapters() {}

    public static <O extends DBObject> SearchAdapter<O> binary(String name) {
        return object -> {
            String objName = object.getName();
            return objName.compareToIgnoreCase(name);
        };
    }

    public static <O extends DBObject> SearchAdapter<O> binary(String name, short overload) {
        return object -> {
            String objName = object.getName();
            short objectOverload = object.getOverload();

            int result = objName.compareToIgnoreCase(name);
            return result == 0 ? objectOverload - overload : result;
        };
    }

    public static <O extends DBObject> SearchAdapter<O> binary(String name, short overload, boolean collection) {
        return object -> {
            if (object instanceof DBType && ((DBType) object).isCollection() == collection) {
                int result = object.getName().compareToIgnoreCase(name);
                return result == 0 ? object.getOverload() - overload : result;
            } else {
                return collection ? -1 : 1;
            }
        };
    }

    public static <O extends DBObject> SearchAdapter<O> linear(String name, Predicate<O> match) {
        return object -> {
              if (match.test(object)) {
                  return object.getName().equalsIgnoreCase(name) ? 0 : 1;
              }
              return -1;
        };
    }
}
