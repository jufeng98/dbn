package com.dbn.driver;

import com.dbn.common.ui.Presentable;
import lombok.Getter;

@Getter
public enum DriverSource implements Presentable{
    @Deprecated // replaced by BUNDLED
    BUILTIN("Built-in library"),

    BUNDLED("Bundled library"),
    EXTERNAL("External library");

    DriverSource(String name) {
        this.name = name;
    }

    private final String name;
}
