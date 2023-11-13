package com.dbn.object.impl;

import com.dbn.object.DBGrantedPrivilege;
import com.dbn.object.DBRole;
import com.dbn.object.common.list.DBObjectRelationImpl;
import com.dbn.object.type.DBObjectRelationType;

class DBRolePrivilegeRelation extends DBObjectRelationImpl<DBRole, DBGrantedPrivilege> {
    public DBRolePrivilegeRelation(DBRole role, DBGrantedPrivilege privilege) {
        super(DBObjectRelationType.ROLE_PRIVILEGE, role, privilege);
    }

    public DBRole getRole() {
        return getSourceObject();
    }

    public DBGrantedPrivilege getPrivilege() {
        return getTargetObject();
    }
}