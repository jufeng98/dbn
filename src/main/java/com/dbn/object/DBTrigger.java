package com.dbn.object;

import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.type.DBTriggerEvent;
import com.dbn.object.type.DBTriggerType;

public interface DBTrigger extends DBSchemaObject {
    boolean isForEachRow();
    DBTriggerType getTriggerType();
    DBTriggerEvent[] getTriggerEvents();

}