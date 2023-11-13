package com.dbn.generator.action;

import com.dbn.browser.DatabaseBrowserManager;
import com.dbn.database.DatabaseFeature;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.dbn.object.DBProgram;
import com.dbn.object.DBTable;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

import java.util.List;

public class GenerateStatementActionGroup extends DefaultActionGroup {

    public GenerateStatementActionGroup(DBObject object) {
        super("Extract SQL Statement", true);
        if (object instanceof DBColumn || object instanceof DBDataset) {
            List<DBObject> selectedObjects = DatabaseBrowserManager.getInstance(object.getProject()).getSelectedObjects();
            add(new GenerateSelectStatementAction(selectedObjects));
        }

        if (object instanceof DBTable) {
            DBTable table = (DBTable) object;
            add(new GenerateInsertStatementAction(table));
        }

        if (object instanceof DBSchemaObject &&
                !(object.getParentObject() instanceof DBProgram) &&
                DatabaseFeature.OBJECT_DDL_EXTRACTION.isSupported(object)) {
            if (getChildrenCount() > 1) {
                addSeparator();
            }
            add(new GenerateDDLStatementAction(object));
        }
    }
}