package com.dbn.connection.config;

import com.dbn.common.filter.Filter;
import com.dbn.common.latent.Latent;
import com.dbn.common.options.CompositeProjectConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.config.ui.ConnectionFilterSettingsForm;
import com.dbn.object.DBColumn;
import com.dbn.object.DBSchema;
import com.dbn.object.common.DBObject;
import com.dbn.object.filter.generic.CompositeColumnFilter;
import com.dbn.object.filter.generic.NonEmptySchemaFilter;
import com.dbn.object.filter.name.ObjectNameFilterSettings;
import com.dbn.object.filter.type.ObjectTypeFilterSettings;
import com.dbn.object.type.DBObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.options.setting.Settings.booleanAttribute;
import static com.dbn.common.options.setting.Settings.setBooleanAttribute;
import static com.dbn.common.util.Unsafe.cast;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ConnectionFilterSettings extends CompositeProjectConfiguration<ConnectionSettings, ConnectionFilterSettingsForm> {
    
    private final @Getter(lazy = true) ObjectTypeFilterSettings objectTypeFilterSettings = new ObjectTypeFilterSettings(this, getConnectionId());
    private final @Getter(lazy = true) ObjectNameFilterSettings objectNameFilterSettings = new ObjectNameFilterSettings(this, getConnectionId());;
    
    private boolean hideEmptySchemas = false;
    private boolean hidePseudoColumns = false;
    private boolean hideAuditColumns = false;

    private transient final Latent<Filter<DBSchema>> schemaFilter = Latent.mutable(
            () -> hideEmptySchemas,
            () -> loadSchemaFilter());

    private transient final Latent<Filter<DBColumn>> columnFilter = Latent.mutable(
            () -> CompositeColumnFilter.signature(hidePseudoColumns, hideAuditColumns),
            () -> loadColumnFilter());

    @Nullable
    private Filter<DBSchema> loadSchemaFilter() {
        Filter<DBSchema> filter = getObjectNameFilterSettings().getFilter(DBObjectType.SCHEMA);
        if (filter == null) {
            return hideEmptySchemas ? NonEmptySchemaFilter.INSTANCE : null;
        } else {
            if (hideEmptySchemas) {
                return schema -> NonEmptySchemaFilter.INSTANCE.accepts(schema) && filter.accepts(schema);
            } else {
                return filter;
            }
        }
    }

    @Nullable
    private Filter<DBColumn> loadColumnFilter() {
        Filter<DBColumn> filter = getObjectNameFilterSettings().getFilter(DBObjectType.COLUMN);
        Filter<DBColumn> compositeFilter = CompositeColumnFilter.get(hidePseudoColumns, hideAuditColumns);
        if (filter == null) {
            return compositeFilter;
        } else {
            if (compositeFilter != null) {
                return column -> compositeFilter.accepts(column) && filter.accepts(column);
            } else {
                return filter;
            }
        }
    }

    ConnectionFilterSettings(ConnectionSettings connectionSettings) {
        super(connectionSettings);
    }

    public ConnectionId getConnectionId() {
        return ensureParent().getConnectionId();
    }

    @Override
    public String getDisplayName() {
        return "Connection Filter Settings";
    }

    @Override
    public String getHelpTopic() {
        return "connectionFilterSettings";
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @NotNull
    @Override
    public ConnectionFilterSettingsForm createConfigurationEditor() {
        return new ConnectionFilterSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "object-filters";
    }

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {
                getObjectTypeFilterSettings(),
                getObjectNameFilterSettings()};
    }

    @Override
    public void readConfiguration(Element element) {
        hideEmptySchemas = booleanAttribute(element, "hide-empty-schemas", hideEmptySchemas);
        hidePseudoColumns = booleanAttribute(element, "hide-pseudo-columns", hidePseudoColumns);
        hideAuditColumns = booleanAttribute(element, "hide-audit-columns", hideAuditColumns);
        super.readConfiguration(element);
    }

    @Override
    public void writeConfiguration(Element element) {
        setBooleanAttribute(element, "hide-empty-schemas", hideEmptySchemas);
        setBooleanAttribute(element, "hide-pseudo-columns", hidePseudoColumns);
        setBooleanAttribute(element, "hide-audit-columns", hideAuditColumns);
        super.writeConfiguration(element);
    }

    @Nullable
    public <T extends DBObject> Filter<T> getNameFilter(DBObjectType objectType) {
        return
            objectType == DBObjectType.SCHEMA ? cast(schemaFilter.get()) :
            objectType == DBObjectType.COLUMN ? cast(columnFilter.get()):
                cast(getObjectNameFilterSettings().getFilter(objectType));
    }
}
