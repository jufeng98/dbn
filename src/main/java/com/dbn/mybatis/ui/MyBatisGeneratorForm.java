package com.dbn.mybatis.ui;

import com.dbn.common.thread.Progress;
import com.dbn.connection.DatabaseType;
import com.dbn.mybatis.settings.MyBatisSettings;
import com.dbn.object.DBTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

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

        setOKButtonText("Generate");

        MyBatisSettings myBatisSettings = MyBatisSettings.getInstance(project);
        generatorSettingsForm = new GeneratorSettingsForm();
        generatorSettingsForm.initForm(myBatisSettings.getGeneratorSettings(), dbTable);

        contentPanel.add(generatorSettingsForm.getMainPanel());

        init();
    }

    protected void doOKAction() {
        if (DatabaseType.MYSQL != dbTable.getConnection().getDatabaseType()) {
            Messages.showInfoMessage("Currently only support MySQL", "Tip");
            super.doOKAction();
            return;
        }

        List<String> errors = generatorSettingsForm.validateParams();
        if (!errors.isEmpty()) {
            Messages.showErrorDialog(String.join("、", errors), "Tip");
            return;
        }

        Progress.background(project, dbTable.getConnection(), false,
                "Tip",
                "Generating " + dbTable.getName() + "...",
                progress -> {
                    generatorSettingsForm.saveAndGenerate();
                    progress.cancel();
                });

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
