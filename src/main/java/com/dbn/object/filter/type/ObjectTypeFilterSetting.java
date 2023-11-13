package com.dbn.object.filter.type;

import com.dbn.common.ui.list.Selectable;
import com.dbn.object.filter.type.ui.ObjectTypeFilterSettingsForm;
import com.dbn.object.type.DBObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Getter
@Setter
@EqualsAndHashCode
public class ObjectTypeFilterSetting implements Selectable<ObjectTypeFilterSetting> {
    private transient ObjectTypeFilterSettings parent;

    private final DBObjectType objectType;
    private boolean selected = true;

    ObjectTypeFilterSetting(ObjectTypeFilterSettings parent, DBObjectType objectType) {
        this.parent = parent;
        this.objectType = objectType;
    }

    ObjectTypeFilterSetting(ObjectTypeFilterSettings parent, DBObjectType objectType, boolean selected) {
        this.parent = parent;
        this.objectType = objectType;
        this.selected = selected;
    }

    @Override
    public Icon getIcon() {
        return objectType.getIcon();
    }

    @Override
    public @NotNull String getName() {
        return objectType.getName().toUpperCase();
    }

    @Override
    public String getError() {
        ObjectTypeFilterSettingsForm settingsEditor = parent.getSettingsEditor();

        boolean masterSettingSelected = isMasterSelected();

        boolean settingSelected =
                (settingsEditor == null && parent.isSelected(this)) ||
                (settingsEditor != null && settingsEditor.isSelected(this));
        if (settingSelected && !masterSettingSelected) {
            return "Disabled on project level";
        }
        return null;
    }

    @Override
    public boolean isMasterSelected() {
        ObjectTypeFilterSettings masterSettings = parent.getMasterSettings();
        if (masterSettings != null) {
            ObjectTypeFilterSettingsForm masterSettingsEditor = masterSettings.getSettingsEditor();
            return masterSettingsEditor == null ?
                    masterSettings.isSelected(this) :
                    masterSettingsEditor.isSelected(this);

        }
        return true;
    }

    @Override
    public int compareTo(@NotNull ObjectTypeFilterSetting o) {
        return 0;
    }
}
