package com.dbn.object.impl;

import com.dbn.object.DBColumn;
import com.dbn.object.DBConstraint;
import com.dbn.object.common.list.DBObjectRelationImpl;
import com.dbn.object.type.DBObjectRelationType;
import lombok.Getter;

@Getter
class DBConstraintColumnRelation extends DBObjectRelationImpl<DBConstraint, DBColumn> {
    private final short position;

    DBConstraintColumnRelation(DBConstraint constraint, DBColumn column, short position) {
        super(DBObjectRelationType.CONSTRAINT_COLUMN, constraint, column);
        this.position = position;
    }

    public DBConstraint getConstraint() {
        return getSourceObject();
    }

    public DBColumn getColumn() {
        return getTargetObject();
    }
}
