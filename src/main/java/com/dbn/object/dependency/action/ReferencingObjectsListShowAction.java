package com.dbn.object.dependency.action;

import com.dbn.object.action.NavigateToObjectAction;
import com.dbn.object.action.ObjectListShowAction;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.AnAction;

import java.util.List;

public class ReferencingObjectsListShowAction extends ObjectListShowAction {
    public ReferencingObjectsListShowAction(DBSchemaObject object) {
        super("Referencing objects", object);
    }

    @Override
    public List<DBObject> getObjectList() {
        return ((DBSchemaObject) getSourceObject()).getReferencingObjects();
    }

    @Override
    public String getTitle() {
        return "Objects referencing " + getSourceObject().getQualifiedNameWithType();
    }

    @Override
    public String getEmptyListMessage() {
        return "No references on " +  getSourceObject().getQualifiedNameWithType() + " found";
    }

    @Override
    public String getListName() {
       return "referencing objects";
    }

    @Override
    protected AnAction createObjectAction(DBObject object) {
        return new NavigateToObjectAction(this.getSourceObject(), object);
    }
    
}