package com.dbn.connection;

import com.dbn.common.constant.Constant;
import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum DatabaseUrlType implements Presentable, Constant<DatabaseUrlType> {
    TNS(nls("cfg.connection.const.DatabaseUrlType_TNS")),
    SID(nls("cfg.connection.const.DatabaseUrlType_SID")),
    SERVICE(nls("cfg.connection.const.DatabaseUrlType_SERVICE")),
    LDAP(nls("cfg.connection.const.DatabaseUrlType_LDAP")),
    LDAPS(nls("cfg.connection.const.DatabaseUrlType_LDAPS")),
    DATABASE(nls("cfg.connection.const.DatabaseUrlType_DATABASE")),
    CUSTOM(nls("cfg.connection.const.DatabaseUrlType_CUSTOM")),
    FILE(nls("cfg.connection.const.DatabaseUrlType_FILE"));

    private final String name;
}
