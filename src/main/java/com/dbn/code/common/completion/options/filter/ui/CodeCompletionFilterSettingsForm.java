package com.dbn.code.common.completion.options.filter.ui;

import com.dbn.code.common.completion.options.filter.CodeCompletionFilterSettings;
import com.dbn.common.color.Colors;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CodeCompletionFilterSettingsForm extends ConfigurationEditorForm<CodeCompletionFilterSettings> {
    private JPanel mainPanel;
    private CodeCompletionFilterTreeModel treeModel;

    public CodeCompletionFilterSettingsForm(CodeCompletionFilterSettings codeCompletionFilterSettings) {
        super(codeCompletionFilterSettings);
        treeModel = new CodeCompletionFilterTreeModel(codeCompletionFilterSettings);
        CodeCompletionFilterTree tree = new CodeCompletionFilterTree(treeModel);
        mainPanel = new JPanel(new BorderLayout());
        JBScrollPane scrollPane = new JBScrollPane(tree);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.setBackground(Colors.getListBackground());
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        treeModel.applyChanges();
    }

    @Override
    public void resetFormChanges() {
        treeModel.resetChanges();
    }
}
