package com.dbn.connection;

import com.dbn.common.constant.Constant;
import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum AuthenticationType implements Constant<AuthenticationType>, Presentable {
    NONE(nls("cfg.connection.const.AuthenticationType_NONE")),
    USER(nls("cfg.connection.const.AuthenticationType_USER")),
    USER_PASSWORD(nls("cfg.connection.const.AuthenticationType_USER_PASSWORD")),
    OS_CREDENTIALS(nls("cfg.connection.const.AuthenticationType_OS_CREDENTIALS"));

    private final String name;
}
