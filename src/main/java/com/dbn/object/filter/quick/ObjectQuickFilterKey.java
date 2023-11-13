package com.dbn.object.filter.quick;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.state.PersistentStateElement;
import com.dbn.connection.ConnectionId;
import com.dbn.object.DBSchema;
import com.dbn.object.type.DBObjectType;
import com.dbn.object.common.list.DBObjectList;
import lombok.Data;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.connectionIdAttribute;
import static com.dbn.common.options.setting.Settings.stringAttribute;

@Data
class ObjectQuickFilterKey implements PersistentStateElement {
    private ConnectionId connectionId;
    private String schemaName;
    private DBObjectType objectType;

    public ObjectQuickFilterKey() {
    }

    private ObjectQuickFilterKey(DBObjectList<?> objectList) {
        connectionId = objectList.getConnection().getConnectionId();
        BrowserTreeNode treeParent = objectList.getParent();
        if (treeParent instanceof DBSchema) {
            schemaName = treeParent.getName();
        } else {
            schemaName = "";
        }
        objectType = objectList.getObjectType();
    }

    public static ObjectQuickFilterKey from(DBObjectList<?> objectList) {
        return new ObjectQuickFilterKey(objectList);
    }

    @Override
    public void readState(Element element) {
        connectionId = connectionIdAttribute(element, "connection-id");
        schemaName = stringAttribute(element, "schema");
        objectType = DBObjectType.get(stringAttribute(element, "object-type"));
    }

    @Override
    public void writeState(Element element) {
        element.setAttribute("connection-id", connectionId.id());
        element.setAttribute("schema", schemaName);
        element.setAttribute("object-type", objectType.getName());
    }
}
