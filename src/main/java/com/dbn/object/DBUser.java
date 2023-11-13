package com.dbn.object;

import com.dbn.object.common.DBRootObject;

import java.util.List;

public interface DBUser extends DBRoleGrantee, DBPrivilegeGrantee, DBRootObject {
    boolean isExpired();
    boolean isLocked();
    boolean isSessionUser();

    List<DBGrantedPrivilege> getPrivileges();
    List<DBGrantedRole> getRoles();

    boolean hasPrivilege(DBPrivilege privilege);
    boolean hasRole(DBRole role);
}
