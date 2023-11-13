package com.dbn.editor.data.action;

import com.dbn.editor.data.filter.DatasetFilterInput;
import com.dbn.object.DBColumn;
import com.dbn.object.action.ObjectListShowAction;
import com.dbn.object.common.DBObject;
import com.intellij.openapi.actionSystem.AnAction;

import java.util.ArrayList;
import java.util.List;

public class ReferencingRecordsOpenAction extends ObjectListShowAction{
    private Object columnValue;

    ReferencingRecordsOpenAction(DBColumn column, Object columnValue) {
        super("Show referencing records...", column);
        this.columnValue = columnValue;
    }


    @Override
    public List<DBObject> getObjectList() {
        DBColumn column = (DBColumn) getSourceObject();
        return new ArrayList<>(column.getReferencingColumns());
    }

    @Override
    public String getTitle() {
        return "Referencing datasets";
    }

    @Override
    public String getEmptyListMessage() {
        return "No referencing records found";
    }

    @Override
    public String getListName() {
        return "Referencing records";
    }

    @Override
    protected AnAction createObjectAction(DBObject object) {
        DBColumn column = (DBColumn) object;
        DatasetFilterInput filterInput = new DatasetFilterInput(column.getDataset());
        filterInput.setColumnValue(column, columnValue);
        String actionText = column.getDataset().getName() + " - " + column.getName();
        return new AbstractRecordsOpenAction(actionText, filterInput) {};
    }
}
