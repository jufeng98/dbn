package com.dbn.object.filter.name;

import com.dbn.object.filter.ConditionOperator;
import com.dbn.object.type.DBObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.stringAttribute;

@Deprecated
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ObjectNameFilter extends CompoundFilterCondition {
    private DBObjectType objectType;
    private final transient ObjectNameFilterSettings settings;

    public ObjectNameFilter(ObjectNameFilterSettings settings) {
        this.settings = settings;
    }

    public ObjectNameFilter(ObjectNameFilterSettings settings, DBObjectType objectType, ConditionOperator operator, String text) {
        this.settings = settings;
        this.objectType = objectType;
        addCondition(operator, text);
    }

    public ObjectNameFilter(ObjectNameFilterSettings settings, DBObjectType objectType, SimpleNameFilterCondition condition) {
        this.settings = settings;
        this.objectType = objectType;
        addCondition(condition);
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        if (getConditions().isEmpty()) {
            settings.removeFilter(this);
        }
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        super.readConfiguration(element);
        objectType = DBObjectType.get(stringAttribute(element, "object-type"));
    }

    @Override
    public void writeConfiguration(Element element) {
        super.writeConfiguration(element);
        element.setAttribute("object-type", objectType.getName());
    }
}
