package com.dbn.object.common.list;

import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import lombok.Getter;

import java.util.List;

@Getter
class DBObjectNavigationListImpl<T extends DBObject> implements DBObjectNavigationList<T> {
    private final String name;
    private final DBObjectRef<T> object;
    private final List<DBObjectRef<T>> objects;
    private ObjectListProvider<T> objectsProvider;

    DBObjectNavigationListImpl(String name, T object) {
        this.name = name;
        this.object = DBObjectRef.of(object);
        this.objects = null;
    }

    DBObjectNavigationListImpl(String name, List<T> objects) {
        this.name = name;
        this.object = null;
        this.objects = DBObjectRef.from(objects);
    }

    DBObjectNavigationListImpl(String name, ObjectListProvider<T> objectsProvider) {
        this.name = name;
        this.object = null;
        this.objects = null;
        this.objectsProvider = objectsProvider;
    }

    @Override
    public T getObject() {
        return DBObjectRef.get(object);
    }

    @Override
    public List<T> getObjects() {
        return objects == null ? null : DBObjectRef.get(objects);
    }

    @Override
    public boolean isLazy() {
        return objectsProvider != null;
    }
}
