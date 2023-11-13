package com.dbn.object;

import com.dbn.object.common.DBRootObject;

public interface DBCharset extends DBRootObject {

    String getDisplayName();
    boolean isDeprecated();
    int getMaxLength();
}