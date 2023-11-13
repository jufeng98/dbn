package com.dbn.object.common.sorting;

import com.dbn.object.common.DBObject;
import com.dbn.object.common.property.DBObjectProperty;
import com.dbn.object.type.DBObjectType;
import com.dbn.object.common.sorting.DBObjectComparators.Key;
import com.dbn.object.lookup.DBObjectRef;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public abstract class DBObjectComparator<T extends DBObject> extends DBObjectComparatorBase<T> {

    @Delegate
    private final Key key;

    protected DBObjectComparator(DBObjectType objectType, SortingType sortingType) {
        this(objectType, null, sortingType, false);
    }

    private DBObjectComparator(DBObjectType objectType, DBObjectProperty objectProperty, SortingType sortingType, boolean virtual) {
        this.key = Key.of(objectType, objectProperty, sortingType, virtual);
    }

    /**
     * Generic all-purpose comparator using the {@link DBObjectRef} compare logic
     */
    static class Generic extends DBObjectComparator<DBObject> {
        protected Generic() {
            super(DBObjectType.ANY, SortingType.NAME);
        }

        @Override
        public int compare(DBObject o1, DBObject o2) {
            return compareRef(o1, o2);
        }
    }

    /**
     * Basic comparator favouring object-type then the object name
     */
    static class Basic extends DBObjectComparator<DBObject> {
        protected Basic(DBObjectType objectType) {
            super(objectType, SortingType.NAME);
        }

        @Override
        public int compare(DBObject o1, DBObject o2) {
            int result = compareType(o1, o2);

            if (result == 0) {
                result = compareName(o1, o2);
            }

            return result;
        }
    }

    /**
     *  Classic comparator considering following attributes in order
     *   - object type
     *   - object name (case-insensitive)
     *   - overload
     */
    static class Classic extends DBObjectComparator<DBObject> {
        protected Classic() {
            super(DBObjectType.ANY, SortingType.NAME);
        }

        @Override
        public int compare(DBObject o1, DBObject o2) {
            int result = compareType(o1, o2);

            if (result == 0) {
                result = compareName(o1, o2);

                if (result == 0) {
                    result = compareOverload(o1, o2);
                }
            }
            return result;
        }
    }

    /**
     *  Compound comparator considering following attributes in order
     *   - object type
     *   - given object property
     *   - object name (case-insensitive)
     *   - overload
     */
    static class Detailed extends DBObjectComparator<DBObject> {
        protected Detailed(DBObjectProperty property) {
            super(DBObjectType.ANY, property, SortingType.NAME, false);
        }

        @Override
        public int compare(DBObject o1, DBObject o2) {
            int result = compareType(o1, o2);

            if (result == 0) {
                result = compareProperty(o1, o2, getProperty());

                if (result == 0) {
                    result = compareName(o1, o2);

                    if (result == 0) {
                        result = compareOverload(o1, o2);
                    }
                }
            }

            return result;
        }
    }


}
