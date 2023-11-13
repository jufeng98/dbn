package com.dbn.object.common.list.action;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.DatabaseEntity;
import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObjectBundle;
import com.dbn.object.type.DBObjectType;
import com.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class ObjectListActionGroup extends DefaultActionGroup {

    public ObjectListActionGroup(DBObjectList objectList) {
        add(new ReloadObjectsAction(objectList));
        DatabaseEntity parentElement = objectList.getParentEntity();
        ConnectionHandler connection = objectList.getConnection();
        if(parentElement instanceof DBSchema) {
            add (new ObjectListFilterAction(objectList));
            addSeparator();
            add (new CreateObjectAction(objectList));
        } else if (parentElement instanceof DBObjectBundle) {
            add (new ObjectListFilterAction(objectList));
            DBObjectType objectType = objectList.getObjectType();
            if (objectType == DBObjectType.SCHEMA) {
                add (new HideEmptySchemasToggleAction(connection));
            }
        } else if (objectList.getObjectType() == DBObjectType.COLUMN) {
            add(new HidePseudoColumnsToggleAction(connection));
            add(new HideAuditColumnsToggleAction(connection));
        }
    }
}