package com.dbn.object.type;

import com.dbn.common.content.DynamicContentType;
import lombok.Getter;

@Getter
public enum DBObjectRelationType implements DynamicContentType<DBObjectRelationType> {
    CONSTRAINT_COLUMN(DBObjectType.CONSTRAINT, DBObjectType.COLUMN),
    INDEX_COLUMN(DBObjectType.INDEX, DBObjectType.COLUMN),
    USER_ROLE(DBObjectType.USER, DBObjectType.GRANTED_ROLE),
    USER_PRIVILEGE(DBObjectType.USER, DBObjectType.GRANTED_PRIVILEGE),
    ROLE_ROLE(DBObjectType.ROLE, DBObjectType.GRANTED_ROLE),
    ROLE_PRIVILEGE(DBObjectType.ROLE, DBObjectType.GRANTED_PRIVILEGE);

    private final DBObjectType sourceType;
    private final DBObjectType targetType;

    DBObjectRelationType(DBObjectType sourceType, DBObjectType targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public boolean matches(DBObjectRelationType contentType) {
        return
            sourceType.matches(contentType.sourceType) &&
            targetType.matches(contentType.targetType);
    }
}
