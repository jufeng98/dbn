package com.dbn.object.filter.custom;

import com.dbn.object.DBColumn;
import com.dbn.object.DBConstraint;
import com.dbn.object.DBTable;
import com.dbn.object.DBView;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.dbn.common.util.Unsafe.cast;

class ObjectFilterAttributeDefinitions {
    private static final Map<DBObjectType, ObjectFilterAttributes> REGISTRY = new HashMap<>();

    @NotNull
    static <T extends DBObject> ObjectFilterAttributes<T> attributesOf(DBObjectType objectType) {
        return cast(REGISTRY.computeIfAbsent(objectType, t -> createDefault(t)));
    }

    private static @NotNull ObjectFilterAttributesImpl<DBObject> createDefault(DBObjectType objectType) {
        return new ObjectFilterAttributesImpl<>(objectType).withAttribute(String.class, objectType.name() + "_NAME", "Object name (literal)", o -> o.getName());
    }

    static {
        create(DBTable.class, DBObjectType.TABLE)
                .withAttribute(String.class,  "TABLE_NAME",     "Table name (literal)", o -> o.getName())
                .withAttribute(Boolean.class, "TEMPORARY",      "Temporary table (boolean)", o -> o.isTemporary());

        create(DBView.class, DBObjectType.VIEW)
                .withAttribute(String.class,  "VIEW_NAME",       "View name (literal)", o -> o.getName())
                .withAttribute(Boolean.class, "SYSTEM_VIEW",     "System level view (boolean)", o -> o.isSystemView());

        create(DBColumn.class, DBObjectType.COLUMN)
                .withAttribute(String.class,  "COLUM_NAME",      "Column name (literal)", o -> o.getName())
                .withAttribute(String.class,  "DATA_TYPE",       "Column data name (literal)", o -> o.getDataType().getName())
                .withAttribute(Integer.class, "DATA_LENGTH",     "Column data length (numeric)", o -> o.getDataType().getLength())
                .withAttribute(Boolean.class, "AUDIT_COLUMN",    "Audit / tracking column (boolean)", o -> o.isAudit())
                .withAttribute(Boolean.class, "PSEUDO_COLUMN",   "Pseudo / hidden column (boolean)", o -> o.isHidden())
                .withAttribute(Boolean.class, "NULLABLE",        "Nullable column (boolean)", o -> o.isNullable())
                .withAttribute(Boolean.class, "IDENTITY",        "Identity column (boolean)", o -> o.isIdentity())
                .withAttribute(Boolean.class, "PRIMARY_KEY",     "Primary key column (boolean)", o -> o.isPrimaryKey())
                .withAttribute(Boolean.class, "FOREIGN_KEY",     "Foreign key column (boolean)", o -> o.isForeignKey());

        create(DBConstraint.class, DBObjectType.CONSTRAINT)
                .withAttribute(String.class,  "CONSTRAINT_NAME", "Constraint name (literal)", o -> o.getName())
                .withAttribute(Boolean.class, "PRIMARY_KEY",     "Primary key column (boolean)", o -> o.isPrimaryKey())
                .withAttribute(Boolean.class, "FOREIGN_KEY",     "Foreign key column (boolean)", o -> o.isForeignKey())
                .withAttribute(Boolean.class, "UNIQUE_KEY",      "Unique key column (boolean)", o -> o.isUniqueKey());

    }

    private static <T extends DBObject> ObjectFilterAttributesImpl<T> create(Class<T> objectClass, DBObjectType objectType) {
        ObjectFilterAttributesImpl<T> attributes = new ObjectFilterAttributesImpl<>(objectType);
        REGISTRY.put(objectType, attributes);
        return attributes;
    }
}
