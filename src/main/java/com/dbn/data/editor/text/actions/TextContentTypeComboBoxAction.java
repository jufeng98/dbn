package com.dbn.data.editor.text.actions;

import com.dbn.common.action.Lookups;
import com.dbn.common.ui.misc.DBNComboBoxAction;
import com.dbn.data.editor.text.TextContentType;
import com.dbn.data.editor.text.ui.TextEditorForm;
import com.dbn.editor.data.options.DataEditorQualifiedEditorSettings;
import com.dbn.editor.data.options.DataEditorSettings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;

public class TextContentTypeComboBoxAction extends DBNComboBoxAction {
    private TextEditorForm editorForm;

    public TextContentTypeComboBoxAction(TextEditorForm editorForm) {
        this.editorForm = editorForm;
        Presentation presentation = getTemplatePresentation();
        TextContentType contentType = editorForm.getContentType();
        presentation.setText(contentType.getName());
        presentation.setIcon(contentType.getIcon());
    }

    @Override
    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        Project project = Lookups.getProject(button);
        DataEditorQualifiedEditorSettings qualifiedEditorSettings = DataEditorSettings.getInstance(project).getQualifiedEditorSettings();
        
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (TextContentType contentType : qualifiedEditorSettings.getContentTypes()) {
            if (contentType.isSelected()) {
                actionGroup.add(new TextContentTypeSelectAction(editorForm, contentType));
            }

        }
        return actionGroup;
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        TextContentType contentType = editorForm.getContentType();
        presentation.setText(contentType.getName());
        presentation.setIcon(contentType.getIcon());
    }
}
