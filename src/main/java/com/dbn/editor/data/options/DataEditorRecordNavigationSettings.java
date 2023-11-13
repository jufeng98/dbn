package com.dbn.editor.data.options;

import com.dbn.common.options.BasicConfiguration;
import com.dbn.common.options.setting.Settings;
import com.dbn.data.record.navigation.RecordNavigationTarget;
import com.dbn.editor.data.options.ui.DataEditorRecordNavigationSettingsForm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DataEditorRecordNavigationSettings extends BasicConfiguration<DataEditorSettings, DataEditorRecordNavigationSettingsForm> {
    private RecordNavigationTarget navigationTarget = RecordNavigationTarget.VIEWER;

    public DataEditorRecordNavigationSettings(DataEditorSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public DataEditorRecordNavigationSettingsForm createConfigurationEditor() {
        return new DataEditorRecordNavigationSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "record-navigation";
    }

    @Override
    public void readConfiguration(Element element) {
        navigationTarget = Settings.getEnum(element, "navigation-target", RecordNavigationTarget.VIEWER);
        if (navigationTarget == RecordNavigationTarget.PROMPT) {
            navigationTarget = RecordNavigationTarget.ASK;
        }
    }

    @Override
    public void writeConfiguration(Element element) {
        Settings.setEnum(element, "navigation-target", navigationTarget);
    }
}
