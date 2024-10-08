package com.dbn.object;

import com.dbn.data.type.DBDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DBColumn extends DBOrderedObject {
    DBDataType getDataType();
    boolean isPrimaryKey();
    boolean isSinglePrimaryKey();
    boolean isForeignKey();
    boolean isUniqueKey();
    boolean isIdentity();
    boolean isNullable();
    boolean isHidden();
    boolean isAudit();
    DBDataset getDataset();
    String getColumnComment();
    String getColumnDefault();
    @Nullable
    DBColumn getForeignKeyColumn();
    List<DBColumn> getReferencingColumns();  // foreign key columns referencing to this
    List<DBIndex> getIndexes();
    List<DBConstraint> getConstraints();

    short getConstraintPosition(DBConstraint constraint);
    DBConstraint getConstraintForPosition(short position);


}

