package com.dbn.driver;

import com.dbn.common.ui.Presentable;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
public enum DriverSource implements Presentable{
    @Deprecated // replaced by BUNDLED
    BUILTIN(nls("cfg.connection.const.DriverSource_BUILTIN")),

    BUNDLED(nls("cfg.connection.const.DriverSource_BUNDLED")),
    EXTERNAL(nls("cfg.connection.const.DriverSource_EXTERNAL"));

    DriverSource(String name) {
        this.name = name;
    }

    private final String name;
}
