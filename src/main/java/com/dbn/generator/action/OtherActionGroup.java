package com.dbn.generator.action;

import com.dbn.object.action.ObjectDropAction;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class OtherActionGroup extends DefaultActionGroup {

    public OtherActionGroup(DBSchemaObject object) {
        super("Other", true);

        add(new ObjectDropAction(object));
    }

}