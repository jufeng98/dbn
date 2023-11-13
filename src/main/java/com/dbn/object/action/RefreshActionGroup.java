package com.dbn.object.action;

import com.dbn.common.icon.Icons;
import com.dbn.database.DatabaseFeature;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.common.list.DBObjectList;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class RefreshActionGroup  extends DefaultActionGroup {
    public RefreshActionGroup(DBObject object) {
        super("Refresh", true);
        getTemplatePresentation().setIcon(Icons.ACTION_REFRESH);
        DBObjectList objectList = (DBObjectList) object.getParent();
        add(new ObjectsReloadAction(objectList));
        if (object instanceof DBSchemaObject && DatabaseFeature.OBJECT_INVALIDATION.isSupported(object)) {
            add(new ObjectsStatusRefreshAction(object.getConnection()));
        }
    }
}
