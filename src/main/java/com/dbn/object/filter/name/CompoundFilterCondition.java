package com.dbn.object.filter.name;

import com.dbn.common.filter.Filter;
import com.dbn.common.util.Strings;
import com.dbn.object.common.DBObject;
import com.dbn.object.filter.ConditionJoinType;
import com.dbn.object.filter.ConditionOperator;
import com.dbn.object.type.DBObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dbn.common.options.setting.Settings.newElement;
import static com.dbn.common.options.setting.Settings.stringAttribute;

@Deprecated
@Getter
@Setter
@EqualsAndHashCode
public class CompoundFilterCondition implements Filter<DBObject>, FilterCondition {
    private ConditionJoinType joinType = ConditionJoinType.AND;
    private final List<FilterCondition> conditions = new ArrayList<>();

    private transient CompoundFilterCondition parent;

    public CompoundFilterCondition() {
    }

    public CompoundFilterCondition(ConditionJoinType joinType) {
        this.joinType = joinType;
    }

    public void addCondition(ConditionOperator operator, String text) {
        SimpleNameFilterCondition condition = new SimpleNameFilterCondition(operator, text);
        addCondition(condition);
    }

    public void addCondition(FilterCondition condition) {
        condition.setParent(this);
        conditions.add(condition);

        ObjectNameFilterSettings settings = getSettings();
        if (settings != null) {
            settings.notifyNodeAdded(conditions.indexOf(condition), condition);
            settings.notifyChildNodesChanged(this);
        }
    }

    public void addCondition(FilterCondition condition, int index) {
        condition.setParent(this);
        conditions.add(index, condition);

        ObjectNameFilterSettings settings = getSettings();
        if (settings != null) {
            settings.notifyNodeAdded(index, condition);
            settings.notifyChildNodesChanged(this);
        }
    }

    public void removeCondition(FilterCondition condition, boolean cleanup) {
        int removeIndex = conditions.indexOf(condition);
        conditions.remove(condition);
        ObjectNameFilterSettings settings = getSettings();
        if (settings != null) {
            settings.notifyNodeRemoved(removeIndex, condition);
            settings.notifyChildNodesChanged(this);
        }

        if (cleanup) cleanup();
    }

    protected void cleanup() {
        if (conditions.size() == 1) {
            if (parent != null) {
                int index = parent.conditions.indexOf(this);
                FilterCondition condition = conditions.get(0);
                parent.addCondition(condition, index);
                parent.removeCondition(this, true);
            }

            FilterCondition filterCondition = conditions.get(0);
            if (filterCondition instanceof CompoundFilterCondition) {
                CompoundFilterCondition compoundFilterCondition = (CompoundFilterCondition) filterCondition;
                this.joinType = compoundFilterCondition.joinType;
                for (FilterCondition childCondition : compoundFilterCondition.conditions) {
                    addCondition(childCondition);
                }
                removeCondition(compoundFilterCondition, true);
            }
        }
    }

    @Override
    public ObjectNameFilterSettings getSettings() {
        return parent == null ? null : parent.getSettings();
    }

    @Override
    public boolean accepts(DBObject object) {
        if (joinType == ConditionJoinType.AND) {
            for (FilterCondition condition : conditions) {
                if (!condition.accepts(object)) return false;
            }
            return true;
        }

        if (joinType == ConditionJoinType.OR) {
            for (FilterCondition condition : conditions) {
                if (condition.accepts(object)) return true;
            }
            return false;
        }

        return false;
    }

    public ConditionJoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(ConditionJoinType joinType) {
        this.joinType = joinType;
    }

    @Override
    public DBObjectType getObjectType() {
        return parent.getObjectType();
    }

    @Override
    public void setParent(CompoundFilterCondition parent) {
        this.parent = parent;
    }

    @Override
    public CompoundFilterCondition getParent() {
        return parent;
    }

    @Override
    public String getConditionString() {
        StringBuilder buffer = new StringBuilder();
        for (FilterCondition condition : conditions) {
            if (buffer.length() > 0) {
                buffer.append(joinType);
            }
            buffer.append(" (");
            buffer.append(condition.getConditionString());
            buffer.append(")");
        }
        return buffer.toString();
    }

    public List<FilterCondition> getConditions() {
        return conditions;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("(");
        for (FilterCondition condition : conditions) {
            if (conditions.indexOf(condition) != 0) {
                buffer.append(" ");
                buffer.append(joinType.toString());
                buffer.append(" ");
            }
            buffer.append(condition.toString());
        }
        buffer.append(")");
        return buffer.toString();
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        String joinTypeString = stringAttribute(element,"join-type");
        joinType = Strings.isEmptyOrSpaces(joinTypeString) ? ConditionJoinType.AND : ConditionJoinType.valueOf(joinTypeString);
        for (Element child : element.getChildren()) {
            FilterCondition condition =
                    Objects.equals(child.getName(), "simple-condition") ? new SimpleNameFilterCondition() :
                    Objects.equals(child.getName(), "compound-condition") ? new CompoundFilterCondition() : null;
            if (condition != null) {
                condition.readConfiguration(child);
                condition.setParent(this);
                conditions.add(condition);
            }
        }
    }

    @Override
    public void writeConfiguration(Element element) {
        element.setAttribute("join-type", joinType.toString());
        for (FilterCondition condition : conditions) {
            Element childElement =
                    condition instanceof SimpleNameFilterCondition ? newElement(element, "simple-condition") :
                    condition instanceof CompoundFilterCondition ? newElement(element, "compound-condition") : null;

            if (childElement != null) {
                condition.writeConfiguration(childElement);
            }
        }

    }
}
