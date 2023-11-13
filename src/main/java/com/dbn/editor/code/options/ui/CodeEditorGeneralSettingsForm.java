package com.dbn.editor.code.options.ui;

import com.dbn.common.event.ProjectEvents;
import com.dbn.common.options.SettingsChangeNotifier;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.editor.code.options.CodeEditorGeneralSettings;
import com.dbn.language.common.SpellcheckingSettingsListener;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CodeEditorGeneralSettingsForm extends ConfigurationEditorForm<CodeEditorGeneralSettings> {
    private JCheckBox showObjectNavigationGutterCheckBox;
    private JCheckBox specDeclarationGutterCheckBox;
    private JPanel mainPanel;
    private JCheckBox enableSpellchecking;
    private JCheckBox enableReferenceSpellchecking;

    public CodeEditorGeneralSettingsForm(CodeEditorGeneralSettings settings) {
        super(settings);
        resetFormChanges();
        registerComponent(mainPanel);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        CodeEditorGeneralSettings configuration = getConfiguration();
        configuration.setShowObjectsNavigationGutter(showObjectNavigationGutterCheckBox.isSelected());
        configuration.setShowSpecDeclarationNavigationGutter(specDeclarationGutterCheckBox.isSelected());

        boolean enableSpellchecking = this.enableSpellchecking.isSelected();
        boolean enableReferenceSpellchecking = this.enableReferenceSpellchecking.isSelected();
        boolean spellcheckingSettingsChanged =
                configuration.isEnableSpellchecking() != enableSpellchecking ||
                configuration.isEnableReferenceSpellchecking() != enableReferenceSpellchecking;

        configuration.setEnableSpellchecking(enableSpellchecking);
        configuration.setEnableReferenceSpellchecking(enableReferenceSpellchecking);

        Project project = configuration.getProject();
        if (spellcheckingSettingsChanged) {
            SettingsChangeNotifier.register(
                    () -> ProjectEvents.notify(project,
                            SpellcheckingSettingsListener.TOPIC,
                            (listener) -> listener.settingsChanged()));
        }
    }

    @Override
    public void resetFormChanges() {
        CodeEditorGeneralSettings settings = getConfiguration();
        showObjectNavigationGutterCheckBox.setSelected(settings.isShowObjectsNavigationGutter());
        specDeclarationGutterCheckBox.setSelected(settings.isShowSpecDeclarationNavigationGutter());
        enableSpellchecking.setSelected(settings.isEnableSpellchecking());
        enableReferenceSpellchecking.setSelected(settings.isEnableReferenceSpellchecking());
    }
}
