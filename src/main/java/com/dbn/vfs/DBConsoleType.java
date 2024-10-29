package com.dbn.vfs;

import com.dbn.common.icon.Icons;
import lombok.Getter;

import javax.swing.*;

@Getter
public enum DBConsoleType {
    STANDARD("SQL Console", Icons.FILE_SQL_CONSOLE),
    MYSQL("MySQL Console", Icons.FILE_SQL_CONSOLE),
    DEBUG("Debug Console", Icons.FILE_SQL_DEBUG_CONSOLE);

    private final String name;
    private final Icon icon;

    DBConsoleType(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

}
