package com.dbn.code.common.completion.options.filter.ui;

import com.dbn.code.common.completion.options.filter.CodeCompletionFilterSettings;
import com.dbn.code.common.completion.options.filter.CodeCompletionFiltersSettings;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.common.ui.util.Keyboard;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.KeymapUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CodeCompletionFiltersSettingsForm extends CompositeConfigurationEditorForm<CodeCompletionFiltersSettings> {

    private JLabel basicCompletionLabel;
    private JLabel extendedCompletionLabel;
    private JPanel mainPanel;
    private JPanel basicFilterPanel;
    private JPanel extendedFilterPanel;

    public CodeCompletionFiltersSettingsForm(CodeCompletionFiltersSettings filtersSettings) {
        super(filtersSettings);
        CodeCompletionFilterSettings basicFilterSettings = filtersSettings.getBasicFilterSettings();
        CodeCompletionFilterSettings extendedFilterSettings = filtersSettings.getExtendedFilterSettings();

        basicFilterPanel.add(basicFilterSettings.createComponent(), BorderLayout.CENTER);
        extendedFilterPanel.add(extendedFilterSettings.createComponent(), BorderLayout.CENTER);

        Shortcut[] basicShortcuts = Keyboard.getShortcuts(IdeActions.ACTION_CODE_COMPLETION);
        Shortcut[] extendedShortcuts = Keyboard.getShortcuts(IdeActions.ACTION_SMART_TYPE_COMPLETION);

        basicCompletionLabel.setText("Basic (" + KeymapUtil.getShortcutsText(basicShortcuts) + ')');
        extendedCompletionLabel.setText("Extended (" + KeymapUtil.getShortcutsText(extendedShortcuts) + ')');
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
