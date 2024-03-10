package com.dbn.object.filter.custom;

import com.dbn.common.expression.ExpressionEvaluator;
import com.dbn.common.expression.GroovyExpressionEvaluator;
import com.dbn.common.options.BasicProjectConfiguration;
import com.dbn.common.util.Lists;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.config.ConnectionFilterSettings;
import com.dbn.object.common.DBObject;
import com.dbn.object.filter.custom.ui.ObjectFilterSettingsForm;
import com.dbn.object.type.DBObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.dbn.common.options.setting.Settings.newElement;
import static com.dbn.common.util.Unsafe.cast;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ObjectCustomFilterSettings extends BasicProjectConfiguration<ConnectionFilterSettings, ObjectFilterSettingsForm> {
    private final ConnectionId connectionId;
    private final List<ObjectFilter<?>> filters = new ArrayList<>();
    private final transient ExpressionEvaluator expressionEvaluator = new GroovyExpressionEvaluator();

    public ObjectCustomFilterSettings(ConnectionFilterSettings parent, ConnectionId connectionId) {
        super(parent);
        this.connectionId = connectionId;
    }

    public <T extends DBObject> ObjectFilter<T> getFilter(DBObjectType objectType) {
        return cast(Lists.first(filters, f -> f.getObjectType() == objectType));
    }

    public void addFilter(ObjectFilter<?> filter) {
        filters.removeIf(f -> f.getObjectType() == filter.getObjectType());
        filters.add(filter);
    }

    public <T extends DBObject> ObjectFilter<T> createFilter(DBObjectType objectType, String expression) {
        ObjectFilter<T> filter = new ObjectFilter<>(this);
        filter.setObjectType(objectType);
        filter.setExpression(expression);

        filters.add(filter);
        return filter;
    }
    public <T extends DBObject> ObjectFilter<?> deleteFilter(DBObjectType objectType) {
        ObjectFilter<T> filter = getFilter(objectType);
        if (filter == null) return null;
        filters.remove(filter);
        return filter;
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @NotNull
    @Override
    public ObjectFilterSettingsForm createConfigurationEditor() {
        return new ObjectFilterSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "object-filters";
    }

    @Override
    public void readConfiguration(Element element) {
        filters.clear();
        for (Element child: element.getChildren()) {
            ObjectFilter<?> filter = new ObjectFilter<>(this);
            filter.readConfiguration(child);

            filters.add(filter);
        }
    }

    @Override
    public void writeConfiguration(Element element) {
        for (ObjectFilter<?> filter : filters) {
            Element child = newElement(element, "filter");
            filter.writeConfiguration(child);
        }

    }

    public ConnectionHandler getConnection() {
        return ConnectionHandler.get(connectionId);
    }
}
