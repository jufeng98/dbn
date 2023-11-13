package com.dbn.object.action;

import com.dbn.common.util.Actions;
import com.dbn.object.common.DBObject;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

import java.util.List;

public class ObjectListActionGroup extends DefaultActionGroup {

    private final ObjectListShowAction listShowAction;
    private final List<DBObject> objects;
    private final List<DBObject> recentObjects;

    public ObjectListActionGroup(ObjectListShowAction listShowAction, List<DBObject> objects, List<DBObject> recentObjects) {
        super("", true);
        this.objects = objects;
        this.recentObjects = recentObjects;
        this.listShowAction = listShowAction;

        if (objects != null) {
            buildNavigationActions();
        }
    }

    private void buildNavigationActions() {
        if (recentObjects != null) {
            for (DBObject object : recentObjects) {
                add(listShowAction.createObjectAction(object));
            }
            add(Actions.SEPARATOR);
        }

        for (DBObject object : objects) {
            if (recentObjects == null || !recentObjects.contains(object))
            add(listShowAction.createObjectAction(object));
        }
    }
}