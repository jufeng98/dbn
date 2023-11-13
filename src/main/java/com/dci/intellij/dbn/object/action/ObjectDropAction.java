package com.dci.intellij.dbn.object.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.factory.DatabaseObjectFactory;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ObjectDropAction extends DumbAwareAction {
    private DBObjectRef<DBSchemaObject> objectRef;

    public ObjectDropAction(DBSchemaObject object) {
        super("Drop...", null, Icons.ACTION_CLOSE);
        objectRef = DBObjectRef.of(object);
    }

    public DBSchemaObject getObject() {
        return DBObjectRef.ensure(objectRef);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DBSchemaObject object = getObject();
        DatabaseObjectFactory objectFactory = DatabaseObjectFactory.getInstance(object.getProject());
        objectFactory.dropObject(object);
    }
}