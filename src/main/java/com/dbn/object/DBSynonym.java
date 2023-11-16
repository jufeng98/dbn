package com.dbn.object;

import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.Nullable;

public interface DBSynonym extends DBSchemaObject {
    @Nullable
    DBObject getUnderlyingObject();

    @Nullable
    DBObjectType getUnderlyingObjectType();

    @Nullable
    static DBObject unwrap(@Nullable DBObject object) {
        if (object == null) return null;

        if (object instanceof DBSynonym) {
            DBSynonym synonym = (DBSynonym) object;
            DBObject underlyingObject = synonym.getUnderlyingObject();
            if (underlyingObject != null) return underlyingObject;
        }
        return object;
    }

}