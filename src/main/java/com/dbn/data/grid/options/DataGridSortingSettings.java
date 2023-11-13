package com.dbn.data.grid.options;

import com.dbn.common.options.BasicProjectConfiguration;
import com.dbn.common.options.setting.Settings;
import com.dbn.data.grid.options.ui.DataGridSortingSettingsForm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DataGridSortingSettings extends BasicProjectConfiguration<DataGridSettings, DataGridSortingSettingsForm> {
    private boolean nullsFirst = true;
    private int maxSortingColumns = 4;

    DataGridSortingSettings(DataGridSettings parent) {
        super(parent);
    }

    /****************************************************
     *                   Configuration                  *
     ****************************************************/
    @Override
    @NotNull
    public DataGridSortingSettingsForm createConfigurationEditor() {
        return new DataGridSortingSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "sorting";
    }

    @Override
    public void readConfiguration(Element element) {
        nullsFirst = Settings.getBoolean(element, "nulls-first", nullsFirst);
        maxSortingColumns = Settings.getInteger(element, "max-sorting-columns", maxSortingColumns);
    }

    @Override
    public void writeConfiguration(Element element) {
        Settings.setBoolean(element, "nulls-first", nullsFirst);
        Settings.setInteger(element, "max-sorting-columns", maxSortingColumns);
    }

}
