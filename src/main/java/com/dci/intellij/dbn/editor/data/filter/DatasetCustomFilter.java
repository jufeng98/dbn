package com.dci.intellij.dbn.editor.data.filter;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.options.setting.Settings;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.data.sorting.SortingState;
import com.dci.intellij.dbn.editor.data.filter.ui.DatasetCustomFilterForm;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.util.text.StringUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dci.intellij.dbn.common.options.setting.Settings.newElement;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DatasetCustomFilter extends DatasetFilterImpl {
    private String condition;

    protected DatasetCustomFilter(DatasetFilterGroup parent, String name) {
        super(parent, name, DatasetFilterType.CUSTOM);
    }

    @Override
    public void generateName() {}

    @Override
    public String getVolatileName() {
        ConfigurationEditorForm configurationEditorForm = getSettingsEditor();
        if (configurationEditorForm != null) {
            DatasetCustomFilterForm customFilterForm = (DatasetCustomFilterForm) configurationEditorForm;
            return customFilterForm.getFilterName();
        }
        return super.getDisplayName();
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public Icon getIcon() {
        return getError() == null ?
                Icons.DATASET_FILTER_CUSTOM :
                Icons.DATASET_FILTER_CUSTOM_ERR;
    }

    @Override
    public String createSelectStatement(DBDataset dataset, SortingState sortingState) {
        setError(null);
        StringBuilder buffer = new StringBuilder();
        DatasetFilterUtil.createSimpleSelectStatement(dataset, buffer);
        buffer.append(" where ");
        buffer.append(condition);
        DatasetFilterUtil.addOrderByClause(dataset, buffer, sortingState);
        return buffer.toString();
    }

    /*****************************************************
     *                   Configuration                   *
     *****************************************************/
    @Override
    @NotNull
    public ConfigurationEditorForm createConfigurationEditor() {
        DBDataset dataset = Failsafe.nn(lookupDataset());
        return new DatasetCustomFilterForm(dataset, this);
    }

    @Override
    public void readConfiguration(Element element) {
        super.readConfiguration(element);
        Element conditionElement = element.getChild("condition");
        condition = Settings.readCdata(conditionElement);
        condition = StringUtil.replace(condition, "<br>", "\n");
        condition = StringUtil.replace(condition, "<sp>", "  ");
    }

    @Override
    public void writeConfiguration(Element element) {
        super.writeConfiguration(element);
        element.setAttribute("type", "custom");
        Element conditionElement = newElement(element, "condition");
        if (this.condition == null) return;

        String condition = StringUtil.replace(this.condition, "\n", "<br>");
        condition = StringUtil.replace(condition, "  ", "<sp>");
        CDATA cdata = new CDATA(condition);
        conditionElement.setContent(cdata);
    }

}
