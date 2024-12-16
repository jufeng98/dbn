package com.dbn.mybatis.ui;

import com.dbn.common.options.ui.CompositeConfigurationEditorForm;
import com.dbn.mybatis.settings.MyBatisSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MyBatisSettingsForm extends CompositeConfigurationEditorForm<MyBatisSettings> {
    private JPanel mainPanel;
    private JPanel generatorPanel;

    public MyBatisSettingsForm(MyBatisSettings settings) {
        super(settings);
        generatorPanel.add(settings.getGeneratorSettings().createComponent(), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
