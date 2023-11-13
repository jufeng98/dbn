package com.dbn.code.sql.style.options.ui;

import com.dbn.code.common.style.options.DBLCodeStyleSettings;
import com.dbn.code.sql.style.options.SQLCodeStyleSettings;
import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class SQLCodeStyleSettingsEditorForm extends CompositeConfigurationEditorForm<DBLCodeStyleSettings> {
    private JPanel mainPanel;
    private JPanel casePanel;
    private JPanel previewPanel;
    private JPanel attributesPanel;

    public SQLCodeStyleSettingsEditorForm(SQLCodeStyleSettings settings) {
        super(settings);
        casePanel.add(settings.getCaseSettings().createComponent(), BorderLayout.CENTER);
        attributesPanel.add(settings.getFormattingSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
