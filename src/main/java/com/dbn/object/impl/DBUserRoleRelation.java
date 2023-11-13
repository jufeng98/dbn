package com.dbn.object.impl;

import com.dbn.object.DBGrantedRole;
import com.dbn.object.DBUser;
import com.dbn.object.type.DBObjectRelationType;
import com.dbn.object.common.list.DBObjectRelationImpl;

class DBUserRoleRelation extends DBObjectRelationImpl<DBUser, DBGrantedRole> {
    public DBUserRoleRelation(DBUser user, DBGrantedRole role) {
        super(DBObjectRelationType.USER_ROLE, user, role);
    }

    public DBUser getUser() {
        return getSourceObject();
    }

    public DBGrantedRole getRole() {
        return getTargetObject();
    }
}