package com.dbn.mybatis.ui;

import com.dbn.common.thread.Progress;
import com.dbn.mybatis.settings.MyBatisSettings;
import com.dbn.object.DBTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyBatisGeneratorForm extends DialogWrapper {
    private final GeneratorSettingsForm generatorSettingsForm;
    private final Project project;
    private final DBTable dbTable;
    private JPanel mainPanel;
    private JPanel contentPanel;

    public MyBatisGeneratorForm(Project project, DBTable dbTable) {
        super(project, false);
        this.project = project;
        this.dbTable = dbTable;
        MyBatisSettings myBatisSettings = MyBatisSettings.getInstance(project);

        generatorSettingsForm = new GeneratorSettingsForm();
        generatorSettingsForm.initForm(myBatisSettings.getGeneratorSettings(), dbTable);

        contentPanel.add(generatorSettingsForm.getMainPanel());

        init();
    }

    protected void doOKAction() {
        Progress.background(project, dbTable.getConnection(), true,
                "温馨提示",
                "正在生成" + dbTable.getName() + "...",
                progress -> generatorSettingsForm.saveAndGenerate());

        super.doOKAction();
    }

    public void doCancelAction() {
        super.doCancelAction();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }
}
