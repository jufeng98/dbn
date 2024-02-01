package com.dbn.common.ui.progress;

import com.dbn.common.color.Colors;
import com.dbn.common.ui.form.DBNFormBase;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ProgressDialogForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JProgressBar progressBar;
    private JLabel progressTitleLabel;
    private JButton backgroundButton;
    private JButton cancelButton;
    private JLabel titleLabel;
    private JPanel titlePanel;

    public ProgressDialogForm(@NotNull ProgressDialog parent, String title, String description) {
        super(parent);

        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        titleLabel.setText(title);
        titlePanel.setBackground(Colors.lafDarker(titlePanel.getBackground(), 1));

        ProgressDialogHandler handler = parent.getHandler();
        progressTitleLabel.setText(handler.getText());

        cancelButton.addActionListener(e -> parent.doCancelAction());
        backgroundButton.addActionListener(e -> parent.doBackgroundAction());
    }

    @Override
    protected JComponent getMainComponent() {
        return mainPanel;
    }
}
