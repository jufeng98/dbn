package com.dbn.object.common.list;

import com.dbn.common.content.DynamicContentElement;
import com.dbn.common.content.DynamicContentType;
import com.dbn.common.dispose.UnlistedDisposable;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectRelationType;

public interface DBObjectRelation<S extends DBObject, T extends DBObject> extends DynamicContentElement, UnlistedDisposable {
    DBObjectRelationType getRelationType();
    S getSourceObject();
    T getTargetObject();

    @Override
    default DynamicContentType getDynamicContentType() {
        return getRelationType();
    }
}
