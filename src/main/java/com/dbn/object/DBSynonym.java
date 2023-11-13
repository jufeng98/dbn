package com.dbn.object;

import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import org.jetbrains.annotations.Nullable;

public interface DBSynonym extends DBSchemaObject {
    @Nullable
    DBObject getUnderlyingObject();
}