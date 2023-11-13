package com.dbn.object.impl;

import com.dbn.object.DBColumn;
import com.dbn.object.DBIndex;
import com.dbn.object.common.list.DBObjectRelationImpl;
import com.dbn.object.type.DBObjectRelationType;

class DBIndexColumnRelation extends DBObjectRelationImpl<DBIndex, DBColumn> {
    DBIndexColumnRelation(DBIndex index, DBColumn column) {
        super(DBObjectRelationType.INDEX_COLUMN, index, column);
    }

    public DBIndex getIndex() {
        return getSourceObject();
    }

    public DBColumn getColumn() {
        return getTargetObject();
    }
}