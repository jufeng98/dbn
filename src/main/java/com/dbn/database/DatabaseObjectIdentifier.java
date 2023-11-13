package com.dbn.database;

import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;

/**
 * Bundles all the information needed to resolve a database object.<br>
 * e.g. if the object represents a column then<br>
 * type = [DatabaseObjectTypes.SCHEMA, DatabaseObjectTypes.TABLE, DatabaseObjectTypes.COLUMN]<br>
 * name = ["SCHEMA_NAME", "TABLE_NAME", "COLUMN_NAME"]<br>
 * representing the column referred as SCHEMA_NAME.TABLE_NAME.COLUMN_NAME
 */
public interface DatabaseObjectIdentifier {
    String[] getObjectNames();

    void setObjectNames(String[] name);

    DBObjectType[] getObjectTypes();

    void setObjectTypes(DBObjectType[] objectTypes);

    String getQualifiedType();
    String getQualifiedName();

    boolean matches(DBObject object);

    String getObjectName(DBObjectType[] objectTypes);

    String getObjectName(DBObjectType objectType);

    int getObjectTypeIndex(DBObjectType[] objectTypes);

    int getObjectTypeIndex(DBObjectType objectType);
}
