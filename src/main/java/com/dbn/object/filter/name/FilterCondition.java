package com.dbn.object.filter.name;

import com.dbn.common.options.PersistentConfiguration;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;

@Deprecated
public interface FilterCondition extends PersistentConfiguration {
    void setParent(CompoundFilterCondition parent);
    CompoundFilterCondition getParent();
    DBObjectType getObjectType();
    String getConditionString();
    ObjectNameFilterSettings getSettings();
    boolean accepts(DBObject object);
}
