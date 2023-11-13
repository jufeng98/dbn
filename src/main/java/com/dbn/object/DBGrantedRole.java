package com.dbn.object;

public interface DBGrantedRole extends DBCastedObject{
    DBRoleGrantee getGrantee();
    DBRole getRole();
    boolean isAdminOption();
    boolean isDefaultRole();

    boolean hasPrivilege(DBPrivilege privilege);
}