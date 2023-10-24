package com.dci.intellij.dbn.plugin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBPluginStatusPair {
    private DBPluginStatus dbn;
    private DBPluginStatus sql;

    public DBPluginStatusPair(DBPluginStatus dbn, DBPluginStatus sql) {
        this.dbn = dbn;
        this.sql = sql;
    }
}
