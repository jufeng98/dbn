package com.dbn.object.common.list;

import com.dbn.common.content.DynamicContent;
import com.dbn.object.type.DBObjectRelationType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DBObjectRelationList<T extends DBObjectRelation> extends DynamicContent<T> {
    DBObjectRelationType getRelationType();
    @NotNull List<T> getObjectRelations();
    List<DBObjectRelation> getRelationBySourceName(String sourceName);
    List<DBObjectRelation> getRelationByTargetName(String targetName);
}
