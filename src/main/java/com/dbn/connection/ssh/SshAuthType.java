package com.dbn.connection.ssh;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum SshAuthType implements Presentable{
    PASSWORD(nls("cfg.connection.const.SshAuthType_PASSWORD")),
    KEY_PAIR(nls("cfg.connection.const.SshAuthType_KEY_PAIR"));

    private final String name;
}
