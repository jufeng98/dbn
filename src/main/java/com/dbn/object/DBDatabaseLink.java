package com.dbn.object;

import com.dbn.object.common.DBSchemaObject;

public interface DBDatabaseLink extends DBSchemaObject {
    String getUserName();
    String getHost();
}