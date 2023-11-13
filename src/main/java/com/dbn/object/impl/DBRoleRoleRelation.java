package com.dbn.object.impl;

import com.dbn.object.DBGrantedRole;
import com.dbn.object.DBRole;
import com.dbn.object.common.list.DBObjectRelationImpl;
import com.dbn.object.type.DBObjectRelationType;

class DBRoleRoleRelation extends DBObjectRelationImpl<DBRole, DBGrantedRole> {
    public DBRoleRoleRelation(DBRole role, DBGrantedRole grantedRole) {
        super(DBObjectRelationType.ROLE_ROLE, role, grantedRole);
    }

    public DBRole getRole() {
        return getSourceObject();
    }

    public DBGrantedRole getGrantedRole() {
        return getTargetObject();
    }
}