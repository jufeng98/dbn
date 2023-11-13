package com.dbn.object;

import com.dbn.object.common.DBRootObject;

import java.util.List;

public interface DBPrivilege extends DBRootObject {

    List<DBUser> getUserGrantees();

    List<DBRole> getRoleGrantees();
}
