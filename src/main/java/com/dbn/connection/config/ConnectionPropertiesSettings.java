package com.dbn.connection.config;

import com.dbn.common.options.BasicProjectConfiguration;
import com.dbn.common.util.Commons;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.config.ui.ConnectionPropertiesSettingsForm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.dbn.common.options.setting.Settings.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ConnectionPropertiesSettings extends BasicProjectConfiguration<ConnectionSettings, ConnectionPropertiesSettingsForm> {
    private Map<String, String> properties = new HashMap<>();
    private boolean enableAutoCommit = true;

    ConnectionPropertiesSettings(ConnectionSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public ConnectionPropertiesSettingsForm createConfigurationEditor() {
        return new ConnectionPropertiesSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "properties";
    }

    @NotNull
    public ConnectionId getConnectionId() {
        return getParent().getConnectionId();
    }

    /*********************************************************
    *                 PersistentConfiguration               *
    *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        enableAutoCommit = getBoolean(element, "auto-commit", enableAutoCommit);
        Element propertiesElement = element.getChild("properties");
        if (propertiesElement != null) {
            for (Element propertyElement : propertiesElement.getChildren()) {
                properties.put(
                        stringAttribute(propertyElement, "key"),
                        stringAttribute(propertyElement, "value"));
            }
        }
        getParent().getDatabaseSettings().updateSignature();
    }

    @Override
    public void writeConfiguration(Element element) {
        setBoolean(element, "auto-commit", enableAutoCommit);
        if (properties.isEmpty()) return;

        Element propertiesElement = newElement(element, "properties");
        for (val entry : properties.entrySet()) {
            Element propertyElement = newElement(propertiesElement, "property");
            propertyElement.setAttribute("key", entry.getKey());
            propertyElement.setAttribute("value", Commons.nvl(entry.getValue(), ""));
        }
    }
}
