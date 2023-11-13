package com.dbn.common.locale;

import com.dbn.common.ui.Presentable;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;

public enum DBDateFormat implements Presentable {
    FULL("Full", DateFormat.FULL),
    SHORT("Short", DateFormat.SHORT),
    MEDIUM("Medium", DateFormat.MEDIUM),
    LONG("Long", DateFormat.LONG),
    CUSTOM("Custom", 0);

    private int dateFormat;
    private String name;

    DBDateFormat(String name, int dateFormat) {
        this.name = name;
        this.dateFormat = dateFormat;
    }

    public int getDateFormat() {
        return dateFormat;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
