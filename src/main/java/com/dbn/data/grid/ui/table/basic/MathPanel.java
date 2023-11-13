package com.dbn.data.grid.ui.table.basic;

import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.util.MathResult;
import com.intellij.ide.IdeTooltipManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

public class MathPanel extends DBNFormBase {
    private JLabel sumLabel;
    private JLabel countLabel;
    private JLabel averageLabel;
    private JPanel mainPanel;

    public MathPanel(Project project, MathResult result) {
        super(null, project);
        sumLabel.setText(result.getSum().toPlainString());
        countLabel.setText(result.getCount().toPlainString());
        averageLabel.setText(result.getAverage().toPlainString());
        Color background = IdeTooltipManager.getInstance().getTextBackground(true);
        mainPanel.setBackground(background);
    }


    @Override
    protected JComponent getMainComponent() {
        return mainPanel;
    }
}
