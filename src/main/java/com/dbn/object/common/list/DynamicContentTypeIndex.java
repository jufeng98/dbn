package com.dbn.object.common.list;

import com.dbn.common.content.DynamicContentType;
import com.dbn.connection.DatabaseType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class DynamicContentTypeIndex<O extends DynamicContentType, T extends DynamicContentType> {

    private final Class<O> ownerType;
    private final Map<DatabaseType, Entry<T>[]> entries = new EnumMap<>(DatabaseType.class);

    public DynamicContentTypeIndex(Class<O> ownerType) {
        this.ownerType = ownerType;
    }

    public int index(DatabaseType databaseType, O ownerType, T type) {
        Entry<T> entry = entry(databaseType, ownerType, (Class<T>) type.getClass());
        return entry.index(type);
    }

    private Entry<T> entry(DatabaseType databaseType, O ownerType, Class<T> type) {
        int position = position(ownerType);
        Entry<T>[] entries = entries(databaseType);
        if (entries[position] == null) {
            synchronized (this) {
                if (entries[position] == null) {
                    entries[position] = new Entry<>(type);
                }
            }
        }
        return entries[position];
    }

    private Entry<T>[] entries(DatabaseType databaseType) {
        Entry<T>[] entries = this.entries.get(databaseType);
        if (entries == null) {
            synchronized (this) {
                entries = this.entries.get(databaseType);
                if (entries == null) {
                    entries = new Entry[ownerType.getEnumConstants().length];
                    this.entries.put(databaseType, entries);
                }
            }
        }
        return entries;
    }

    private static class Entry<T extends DynamicContentType> {
        private final Class<T> type;
        private final int[] indexes;
        private int size = 0;

        public Entry(Class<T> type) {
            this.type = type;
            this.indexes = new int[type.getEnumConstants().length];
            Arrays.fill(indexes, -1);
        }

        public int index(T type) {
            int position = position(type);
            if (indexes[position] == -1) {
                synchronized (this) {
                    if (indexes[position] == -1) {
                        indexes[position] = size;
                        size++;
                    }
                }
            }

            return indexes[position];
        }

        @Override
        public String toString() {
            return type.getSimpleName() + " " + size;
        }
    }

    public String toString() {
        return ownerType.getSimpleName();
    }


    private static int position(DynamicContentType type) {
        Enum e = (Enum) type;
        return e.ordinal();
    }
}
