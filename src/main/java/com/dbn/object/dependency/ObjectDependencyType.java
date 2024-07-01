package com.dbn.object.dependency;

import com.dbn.common.icon.Icons;
import com.dbn.common.ui.Presentable;
import lombok.Getter;

import javax.swing.*;

import static com.dbn.nls.NlsResources.nls;

@Getter
public enum ObjectDependencyType implements Presentable{
    INCOMING(nls("app.objects.const.ObjectDependencyType_INCOMING"), Icons.DBO_INCOMING_REF, Icons.DBO_INCOMING_REF_SOFT),
    OUTGOING(nls("app.objects.const.ObjectDependencyType_OUTGOING"), Icons.DBO_OUTGOING_REF, Icons.DBO_OUTGOING_REF_SOFT);

    private final String name;
    private final Icon icon;
    private final Icon softIcon;

    ObjectDependencyType(String name, Icon icon, Icon softIcon) {
        this.name = name;
        this.icon = icon;
        this.softIcon = softIcon;
    }
}
