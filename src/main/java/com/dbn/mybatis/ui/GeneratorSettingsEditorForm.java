package com.dbn.mybatis.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.mybatis.settings.GeneratorSettings;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class GeneratorSettingsEditorForm extends ConfigurationEditorForm<GeneratorSettings> {
    private final GeneratorSettingsForm generatorSettingsForm;

    public GeneratorSettingsEditorForm(GeneratorSettings settings) {
        super(settings);

        generatorSettingsForm = new GeneratorSettingsForm();
        generatorSettingsForm.initForm(settings, null);

        resetFormChanges();
        registerComponents(getMainComponent());
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        generatorSettingsForm.apply();
    }

    @Override
    public void resetFormChanges() {
        generatorSettingsForm.reset();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return generatorSettingsForm.getMainPanel();
    }
}
