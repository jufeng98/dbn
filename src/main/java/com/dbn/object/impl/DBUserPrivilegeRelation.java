package com.dbn.object.impl;

import com.dbn.object.DBGrantedPrivilege;
import com.dbn.object.DBUser;
import com.dbn.object.common.list.DBObjectRelationImpl;
import com.dbn.object.type.DBObjectRelationType;

class DBUserPrivilegeRelation extends DBObjectRelationImpl<DBUser, DBGrantedPrivilege> {
    public DBUserPrivilegeRelation(DBUser user, DBGrantedPrivilege privilege) {
        super(DBObjectRelationType.USER_PRIVILEGE, user, privilege);
    }

    public DBUser getUser() {
        return getSourceObject();
    }

    public DBGrantedPrivilege getPrivilege() {
        return getTargetObject();
    }
}