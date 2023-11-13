package com.dbn.execution.method.browser;

import com.dbn.common.options.PersistentConfiguration;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.config.ConnectionConfigListener;
import com.dbn.object.DBMethod;
import com.dbn.object.DBSchema;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
import lombok.val;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.dbn.common.options.setting.Settings.*;

public class MethodBrowserSettings implements PersistentConfiguration, ConnectionConfigListener {
    private DBObjectRef<DBMethod> selectedMethod;
    private ConnectionId selectedConnectionId;
    private String selectedSchema;
    private final Map<DBObjectType, Boolean> objectVisibility = new EnumMap<>(DBObjectType.class);

    public MethodBrowserSettings() {
        objectVisibility.put(DBObjectType.FUNCTION, true);
        objectVisibility.put(DBObjectType.PROCEDURE, true);
    }

    @Nullable
    public ConnectionHandler getConnection() {
        return ConnectionHandler.get(selectedConnectionId);
    }

    public void setSelectedConnection(ConnectionHandler connection) {
        this.selectedConnectionId = connection == null ? null : connection.getConnectionId();
    }

    public DBSchema getSelectedSchema() {
        return getConnection() == null || selectedSchema == null ? null : getConnection().getObjectBundle().getSchema(selectedSchema);
    }

    public Set<DBObjectType> getVisibleObjectTypes() {
        Set<DBObjectType> objectTypes = EnumSet.noneOf(DBObjectType.class);

        for (val entry : objectVisibility.entrySet()) {
            DBObjectType objectType = entry.getKey();
            Boolean visible = entry.getValue();

            if (visible) {
                objectTypes.add(objectType);
            }
        }
        return objectTypes;
    }

    public boolean getObjectVisibility(DBObjectType objectType) {
        return objectVisibility.get(objectType);
    }

    public boolean setObjectVisibility(DBObjectType objectType, boolean visibility) {
        if (getObjectVisibility(objectType) != visibility) {
            objectVisibility.put(objectType, visibility);
            return true;
        }
        return false;        
    }

    public void setSelectedSchema(DBSchema schema) {
        this.selectedSchema = schema == null ? null : schema.getName();
    }

    @Nullable
    public DBMethod getSelectedMethod() {
        return selectedMethod == null ? null : selectedMethod.get();
    }

    public void setSelectedMethod(DBMethod method) {
        this.selectedMethod = DBObjectRef.of(method);
    }

    public void connectionRemoved(ConnectionId connectionId) {
        if (connectionId.equals(selectedConnectionId)) {
            selectedConnectionId = null;
            selectedSchema = null;
        }
        if (selectedMethod != null && selectedMethod.getConnectionId().equals(connectionId)) {
            selectedMethod = null;
        }
    }

    @Override
    public void readConfiguration(Element element) {
        selectedConnectionId = connectionIdAttribute(element, "connection-id");
        selectedSchema = stringAttribute(element, "schema");

        Element methodElement = element.getChild("selected-method");
        if (methodElement != null) {
            selectedMethod = new DBObjectRef<>();
            selectedMethod.readState(methodElement);
        }
    }

    @Override
    public void writeConfiguration(Element element) {
        ConnectionHandler connection = getConnection();
        if (connection != null) element.setAttribute("connection-id", connection.getConnectionId().id());
        if (selectedSchema != null) element.setAttribute("schema", selectedSchema);
        if(selectedMethod != null) {
            Element methodElement = newElement(element, "selected-method");
            selectedMethod.writeState(methodElement);
        }
    }
}
