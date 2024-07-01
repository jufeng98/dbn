package com.dbn.driver;

import com.dbn.common.ui.Presentable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum DriverSource implements Presentable{
    @Deprecated // replaced by BUNDLED
    BUILTIN(nls("cfg.connection.const.DriverSource_BUILTIN")),

    BUNDLED(nls("cfg.connection.const.DriverSource_BUNDLED")),
    EXTERNAL(nls("cfg.connection.const.DriverSource_EXTERNAL"));

    private final String name;
}
