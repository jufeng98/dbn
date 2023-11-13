package com.dbn.object.dependency.action;

import com.dbn.object.action.NavigateToObjectAction;
import com.dbn.object.action.ObjectListShowAction;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.AnAction;

import java.util.List;

public class ReferencedObjectsListShowAction extends ObjectListShowAction {
    public ReferencedObjectsListShowAction(DBSchemaObject object) {
        super("Referenced objects", object);
    }

    @Override
    public List<DBObject> getObjectList() {
        return ((DBSchemaObject) getSourceObject()).getReferencedObjects();
    }

    @Override
    public String getTitle() {
        return "Objects referenced by " + getSourceObject().getQualifiedNameWithType();
    }

    @Override
    public String getEmptyListMessage() {
        return "No referenced objects found for " + getSourceObject().getQualifiedNameWithType();
    }


    @Override
    public String getListName() {
       return "referenced objects";
   }

    @Override
    protected AnAction createObjectAction(DBObject object) {
        return new NavigateToObjectAction(this.getSourceObject(), object);
    }

}