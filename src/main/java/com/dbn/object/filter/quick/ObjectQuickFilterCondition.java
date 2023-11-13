package com.dbn.object.filter.quick;

import com.dbn.common.state.PersistentStateElement;
import com.dbn.object.common.DBObject;
import com.dbn.object.filter.ConditionOperator;
import com.dbn.object.filter.NameFilterCondition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.booleanAttribute;
import static com.dbn.common.options.setting.Settings.setBooleanAttribute;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ObjectQuickFilterCondition extends NameFilterCondition implements PersistentStateElement {
    private transient ObjectQuickFilter<?> filter;
    private boolean active = true;

    public ObjectQuickFilterCondition(ObjectQuickFilter<?> filter, ConditionOperator operator, String pattern, boolean active) {
        super(operator, pattern);
        this.filter = filter;
        this.active = active;
    }

    public ObjectQuickFilterCondition(ObjectQuickFilter<?> filter) {
        this.filter = filter;
    }

    public boolean accepts(DBObject object) {
        return accepts(object.getName());
    }

    public int index() {
        return filter.getConditions().indexOf(this);
    }

    @Override
    public void readState(Element element) {
        super.readState(element);
        active = booleanAttribute(element, "active", true);
    }

    @Override
    public void writeState(Element element) {
        super.writeState(element);
        setBooleanAttribute(element, "active", active);
    }
}
