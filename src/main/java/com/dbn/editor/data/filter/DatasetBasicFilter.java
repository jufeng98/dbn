package com.dbn.editor.data.filter;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.icon.Icons;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.util.Lists;
import com.dbn.data.sorting.SortingState;
import com.dbn.editor.data.filter.ui.DatasetBasicFilterForm;
import com.dbn.object.DBDataset;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dbn.common.options.setting.Settings.enumAttribute;
import static com.dbn.common.options.setting.Settings.newElement;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DatasetBasicFilter extends DatasetFilterImpl {
    private final List<DatasetBasicFilterCondition> conditions = new ArrayList<>();
    private ConditionJoinType joinType = ConditionJoinType.AND;


    DatasetBasicFilter(DatasetFilterGroup parent, String name) {
        super(parent, name, DatasetFilterType.BASIC);
    }

    @Override
    public void generateName() {
        if (!isCustomNamed()) {
            boolean addSeparator = false;
            StringBuilder buffer = new StringBuilder();
            for (DatasetBasicFilterCondition condition : conditions) {
                if (condition.isActive() && condition.getValue().trim().length() > 0) {
                    if (addSeparator) buffer.append(joinType == ConditionJoinType.AND ? " & " : " | ");
                    addSeparator = true;
                    buffer.append(condition.getValue());
                    if (buffer.length() > 40) {
                        buffer.setLength(40);
                        buffer.append("...");
                        break;
                    }
                }
            }

            String name =  buffer.length() > 0 ? buffer.toString() : getFilterGroup().createFilterName("Filter");
            setName(name);
        }
    }

    void addCondition(String columnName, Object value, ConditionOperator operator) {
        DatasetBasicFilterCondition condition = new DatasetBasicFilterCondition(this, columnName, value, operator, true);
        addCondition(condition);
    }

    public void addCondition(DatasetBasicFilterCondition condition) {
        conditions.add(condition);
    }

    public boolean containsConditionForColumn(String columnName) {
        return Lists.anyMatch(conditions, condition -> Objects.equals(condition.getColumnName(), columnName));
    }

    @Override
    public String getVolatileName() {
        ConfigurationEditorForm configurationEditorForm = getSettingsEditor();
        if (configurationEditorForm != null) {
            DatasetBasicFilterForm basicFilterForm = (DatasetBasicFilterForm) configurationEditorForm;
            return basicFilterForm.getFilterName();
        }
        return super.getDisplayName();
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public Icon getIcon() {
        return  isTemporary() ? (
                    getError() == null ?
                        Icons.DATASET_FILTER_BASIC_TEMP : 
                        Icons.DATASET_FILTER_BASIC_TEMP_ERR) :
                    getError() == null ?
                        Icons.DATASET_FILTER_BASIC :
                        Icons.DATASET_FILTER_BASIC_ERR;
    }

    @Override
    public String createSelectStatement(DBDataset dataset, SortingState sortingState, Integer pageNum, Integer pageSize) {
        setError(null);
        StringBuilder buffer = new StringBuilder();
        DatasetFilterUtil.createSimpleSelectStatement(dataset, buffer);
        boolean initialized = false;
        for (DatasetBasicFilterCondition condition : conditions) {
            if (condition.isActive()) {
                if (!initialized) {
                    buffer.append(" where ");
                    initialized = true;
                } else {
                    switch (joinType) {
                        case AND: buffer.append(" and "); break;
                        case OR: buffer.append(" or "); break;
                    }
                }
                condition.appendConditionString(buffer, dataset);
            }
        }

        DatasetFilterUtil.addOrderByClause(dataset, buffer, sortingState);

        String limit = DatasetFilterUtil.createLimit(dataset, pageNum, pageSize);
        buffer.append(limit);

        return buffer.toString();
    }

    /****************************************************
     *                   Configuration                  *
     ****************************************************/
   @Override
   @NotNull
   public ConfigurationEditorForm createConfigurationEditor() {
       DBDataset dataset = Failsafe.nn(lookupDataset());
       return new DatasetBasicFilterForm(dataset, this);
   }

   @Override
   public void readConfiguration(Element element) {
       super.readConfiguration(element);
       joinType = enumAttribute(element, "join-type", ConditionJoinType.AND);
       for (Element child : element.getChildren()) {
           DatasetBasicFilterCondition condition = new DatasetBasicFilterCondition(this);
           condition.readConfiguration(child);
           conditions.add(condition);
       }
   }

    @Override
    public void writeConfiguration(Element element) {
        super.writeConfiguration(element);
        element.setAttribute("type", "basic");
        element.setAttribute("join-type", joinType.name());
        for (DatasetBasicFilterCondition condition: conditions) {
            Element conditionElement = newElement(element, "condition");
            condition.writeConfiguration(conditionElement);
        }
    }
}
