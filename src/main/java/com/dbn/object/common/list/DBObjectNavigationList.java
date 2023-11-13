package com.dbn.object.common.list;

import com.dbn.object.common.DBObject;

import java.util.List;

public interface DBObjectNavigationList<T extends DBObject> {
    DBObjectNavigationList[] EMPTY_ARRAY = new DBObjectNavigationList[0];

    String getName();
    T getObject();
    List<T> getObjects();
    ObjectListProvider<T> getObjectsProvider();
    boolean isLazy();


    static <T extends DBObject> DBObjectNavigationList<T> create(String name, T object) {
        return new DBObjectNavigationListImpl<>(name, object);
    }

    static <T extends DBObject> DBObjectNavigationList<T> create(String name, List<T> objects) {
        return new DBObjectNavigationListImpl<>(name, objects);
    }

    static <T extends DBObject> DBObjectNavigationList<T> create(String name, ObjectListProvider<T> objectsProvider) {
        return new DBObjectNavigationListImpl<>(name, objectsProvider);
    }
}
