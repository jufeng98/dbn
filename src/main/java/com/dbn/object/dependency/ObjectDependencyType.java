package com.dbn.object.dependency;

import com.dbn.common.icon.Icons;
import com.dbn.common.ui.Presentable;
import lombok.Getter;

import javax.swing.*;

@Getter
public enum ObjectDependencyType implements Presentable{
    INCOMING("Incoming references", "objects used by this", Icons.DBO_INCOMING_REF, Icons.DBO_INCOMING_REF_SOFT),
    OUTGOING("Outgoing references", "objects using this", Icons.DBO_OUTGOING_REF, Icons.DBO_OUTGOING_REF_SOFT);

    private final String name;
    private String description;
    private final Icon icon;
    private final Icon softIcon;

    ObjectDependencyType(String name, String description, Icon icon, Icon softIcon) {
        this.name = name;
        this.icon = icon;
        this.softIcon = softIcon;
    }
}
