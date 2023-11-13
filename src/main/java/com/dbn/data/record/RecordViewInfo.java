package com.dbn.data.record;

import lombok.Getter;

import javax.swing.*;

public class RecordViewInfo {
    private final @Getter String title;
    private final @Getter Icon icon;

    public RecordViewInfo(String title, Icon icon) {
        this.title = title;
        this.icon = icon;
    }
}
