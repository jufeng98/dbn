package com.dbn.common.environment.options;

import com.dbn.common.environment.EnvironmentType;
import com.dbn.common.environment.EnvironmentTypeBundle;
import com.dbn.common.environment.EnvironmentTypeId;
import com.dbn.common.environment.options.ui.EnvironmentSettingsForm;
import com.dbn.common.options.BasicProjectConfiguration;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.options.general.GeneralProjectSettings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.dbn.common.options.setting.Settings.newElement;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class EnvironmentSettings extends BasicProjectConfiguration<GeneralProjectSettings, ConfigurationEditorForm<?>> {
    private EnvironmentTypeBundle environmentTypes = new EnvironmentTypeBundle(EnvironmentTypeBundle.DEFAULT);
    private EnvironmentVisibilitySettings visibilitySettings = new EnvironmentVisibilitySettings();

    public EnvironmentSettings(GeneralProjectSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public ConfigurationEditorForm<?> createConfigurationEditor() {
        return new EnvironmentSettingsForm(this);
    }

    @NotNull
    public EnvironmentType getEnvironmentType(EnvironmentTypeId environmentTypeId) {
        return environmentTypes.getEnvironmentType(environmentTypeId);
    }

    public boolean setEnvironmentTypes(EnvironmentTypeBundle environmentTypes) {
        boolean changed = !Objects.equals(this.environmentTypes, environmentTypes);
        this.environmentTypes = new EnvironmentTypeBundle(environmentTypes);
        return changed;
    }

    @Override
    public String getConfigElementName() {
        return "environment";
    }

    @Override
    public void readConfiguration(Element element) {
        Element environmentTypesElement = element.getChild("environment-types");
        if (environmentTypesElement != null) {
            environmentTypes.clear();
            for (Element child : environmentTypesElement.getChildren()) {
                EnvironmentType environmentType = new EnvironmentType(null);
                environmentType.readConfiguration(child);
                environmentTypes.add(environmentType);
            }
        }

        Element visibilitySettingsElement = element.getChild("visibility-settings");
        if (visibilitySettingsElement != null) {
            visibilitySettings.readConfiguration(visibilitySettingsElement);
        }
    }

    @Override
    public void writeConfiguration(Element element) {
        Element environmentTypesElement = newElement(element, "environment-types");
        for (EnvironmentType environmentType : environmentTypes) {
            Element itemElement = newElement(environmentTypesElement, "environment-type");
            environmentType.writeConfiguration(itemElement);
        }

        Element visibilitySettingsElement = newElement(element, "visibility-settings");
        visibilitySettings.writeConfiguration(visibilitySettingsElement);
    }
}
