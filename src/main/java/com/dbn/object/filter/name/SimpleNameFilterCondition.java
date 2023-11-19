package com.dbn.object.filter.name;

import com.dbn.object.common.DBObject;
import com.dbn.object.filter.ConditionOperator;
import com.dbn.object.filter.NameFilterCondition;
import com.dbn.object.type.DBObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.enumAttribute;
import static com.dbn.common.util.Strings.cachedUpperCase;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SimpleNameFilterCondition extends NameFilterCondition implements FilterCondition {
    private transient CompoundFilterCondition parent;

    public SimpleNameFilterCondition() {}

    public SimpleNameFilterCondition(ConditionOperator operator, String pattern) {
        super(operator, pattern);
    }

    @Override
    public ObjectNameFilterSettings getSettings() {
        return parent.getSettings();
    }

    @Override
    public boolean accepts(DBObject object) {
        return accepts(object.getName());
    }

    @Override
    public DBObjectType getObjectType() {
        return parent.getObjectType();
    }

    @Override
    public String getConditionString() {
        return "OBJECT_NAME " + getOperator() + " '" + getPattern() + "'";
    }

    public String toString() {
        return cachedUpperCase(getObjectType().getName()) + "_NAME " + getOperator() + " '" + getPattern() + "'";
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        setOperator(enumAttribute(element, "operator", ConditionOperator.class));
        setPattern(element.getAttributeValue("text"));
    }

    @Override
    public void writeConfiguration(Element element) {
        element.setAttribute("operator", getOperator().name());
        element.setAttribute("text", getPattern());
    }
}
