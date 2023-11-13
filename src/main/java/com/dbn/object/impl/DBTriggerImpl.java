package com.dbn.object.impl;

import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.metadata.def.DBTriggerMetadata;
import com.dbn.editor.DBContentType;
import com.dbn.object.DBDataset;
import com.dbn.object.DBSchema;
import com.dbn.object.DBTrigger;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObjectImpl;
import com.dbn.object.common.operation.DBOperationExecutor;
import com.dbn.object.common.operation.DatabaseOperationManager;
import com.dbn.object.common.status.DBObjectStatus;
import com.dbn.object.common.status.DBObjectStatusHolder;
import com.dbn.object.properties.PresentableProperty;
import com.dbn.object.properties.SimplePresentableProperty;
import com.dbn.object.type.DBTriggerEvent;
import com.dbn.object.type.DBTriggerType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.dbn.object.common.property.DBObjectProperty.*;
import static com.dbn.object.type.DBTriggerEvent.*;
import static com.dbn.object.type.DBTriggerType.*;

abstract class DBTriggerImpl extends DBSchemaObjectImpl<DBTriggerMetadata> implements DBTrigger {
    private DBTriggerType triggerType;
    private DBTriggerEvent[] triggerEvents;

    DBTriggerImpl(DBSchema schema, DBTriggerMetadata metadata) throws SQLException {
        super(schema, metadata);
    }

    DBTriggerImpl(DBDataset dataset, DBTriggerMetadata metadata) throws SQLException {
        super(dataset, metadata);
    }

    @Override
    protected String initObject(ConnectionHandler connection, DBObject parentObject, DBTriggerMetadata metadata) throws SQLException {
        String name = metadata.getTriggerName();
        set(FOR_EACH_ROW, metadata.isForEachRow());

        String triggerTypeString = metadata.getTriggerType();
        triggerType =
                triggerTypeString.contains("BEFORE") ? BEFORE :
                triggerTypeString.contains("AFTER") ? AFTER :
                triggerTypeString.contains("INSTEAD OF") ? INSTEAD_OF :
                        DBTriggerType.UNKNOWN;


        String triggeringEventString = metadata.getTriggeringEvent();
        List<DBTriggerEvent> eventList = new ArrayList<>();
        if (triggeringEventString.contains("INSERT")) eventList.add(INSERT);
        if (triggeringEventString.contains("UPDATE")) eventList.add(UPDATE);
        if (triggeringEventString.contains("DELETE")) eventList.add(DELETE);
        if (triggeringEventString.contains("TRUNCATE")) eventList.add(TRUNCATE);
        if (triggeringEventString.contains("CREATE")) eventList.add(CREATE);
        if (triggeringEventString.contains("ALTER")) eventList.add(ALTER);
        if (triggeringEventString.contains("DROP")) eventList.add(DROP);
        if (triggeringEventString.contains("RENAME")) eventList.add(RENAME);
        if (triggeringEventString.contains("LOGON")) eventList.add(LOGON);
        if (triggeringEventString.contains("DDL")) eventList.add(DDL);
        if (eventList.size() == 0) eventList.add(DBTriggerEvent.UNKNOWN);

        triggerEvents = eventList.toArray(new DBTriggerEvent[0]);
        return name;
    }

    @Override
    public void initStatus(DBTriggerMetadata metadata) throws SQLException {
        DBObjectStatusHolder objectStatus = getStatus();
        objectStatus.set(DBObjectStatus.ENABLED, metadata.isEnabled());
        objectStatus.set(DBObjectStatus.VALID, metadata.isValid());
        objectStatus.set(DBObjectStatus.DEBUG, metadata.isDebug());
    }

    @Override
    public void initProperties() {
        properties.set(EDITABLE, true);
        properties.set(DISABLEABLE, true);
        properties.set(REFERENCEABLE, true);
        properties.set(COMPILABLE, true);
        properties.set(DEBUGABLE, true);
        properties.set(INVALIDABLE, true);
        properties.set(SCHEMA_OBJECT, true);
    }

    @Override
    public boolean isForEachRow() {
        return is(FOR_EACH_ROW);
    }

    @Override
    public DBTriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public DBTriggerEvent[] getTriggerEvents() {
        return triggerEvents;
    }

    @Override
    public DBOperationExecutor getOperationExecutor() {
        return operationType -> {
            DatabaseOperationManager operationManager = DatabaseOperationManager.getInstance(getProject());
            switch (operationType) {
                case ENABLE:  operationManager.enableTrigger(this); break;
                case DISABLE: operationManager.disableTrigger(this); break;
            }
        };
    }

    @Override
    public List<PresentableProperty> getPresentableProperties() {
        List<PresentableProperty> properties = super.getPresentableProperties();
        StringBuilder events = new StringBuilder(triggerType.getName().toLowerCase());
        events.append(" ");
        for (DBTriggerEvent triggeringEvent : triggerEvents) {
            if (triggeringEvent != triggerEvents[0]) events.append(" or ");
            events.append(triggeringEvent.getName().toUpperCase());
        }

        properties.add(0, new SimplePresentableProperty("Trigger event", events.toString()));
        return properties;
    }

    /*********************************************************
     *                     TreeElement                       *
     *********************************************************/

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String getCodeParseRootId(DBContentType contentType) {
        return "trigger_definition";
    }
}
