package com.dbn.object.filter.custom.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.util.ComponentAligner;
import com.dbn.common.util.Actions;
import com.dbn.language.sql.SQLFileType;
import com.dbn.object.filter.custom.ObjectFilter;
import com.dbn.object.filter.custom.ui.action.DeleteObjectFilterAction;
import com.dbn.object.filter.custom.ui.action.EditObjectFilterAction;
import com.dbn.object.filter.custom.ui.action.ToggleObjectFilterStatusAction;
import com.dbn.object.type.DBObjectType;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static com.dbn.common.util.Strings.cachedUpperCase;

public class ObjectFilterExpressionForm extends DBNFormBase implements ComponentAligner.Form {
    private JPanel mainPanel;
    private JLabel objectTypeLabel;
    private JPanel actionsPanel;
    private EditorTextField expressionTextField;

    private final ObjectFilter<?> filter;

    public ObjectFilterExpressionForm(ObjectFilterSettingsForm parent, ObjectFilter<?> filter) {
        super(parent);
        this.filter = filter;

        DBObjectType objectType = filter.getObjectType();
        objectTypeLabel.setIcon(objectType.getIcon());
        objectTypeLabel.setText(cachedUpperCase(objectType.getName()));


        expressionTextField.setText(normalizeExpression(filter.getExpression()));
        expressionTextField.addSettingsProvider(createEditorSettingsProvider());

        actionsPanel.add(createActionToolbar(), BorderLayout.CENTER);
        updateEditorField();
    }

    private JComponent createActionToolbar() {
        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel,
                "DBNavigator.ObjectFilter.Expression", true,
                new ToggleObjectFilterStatusAction(this),
                new DeleteObjectFilterAction(this),
                new EditObjectFilterAction(this));
        return actionToolbar.getComponent();
    }

    private @NotNull EditorSettingsProvider createEditorSettingsProvider() {
        return editor -> {
            SyntaxHighlighter syntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(SQLFileType.INSTANCE, getProject(), null);
            EditorColorsScheme colorsScheme = editor.getColorsScheme();
            EditorHighlighter highlighter = HighlighterFactory.createHighlighter(syntaxHighlighter, colorsScheme);
            editor.setHighlighter(highlighter);
            editor.setViewer(true);

            editor.setBackgroundColor(getFilter().isActive() ?
                    Colors.getTextFieldBackground() :
                    Colors.getTextFieldDisabledBackground());
        };
    }

    public void setExpression(String expression) {
        expression = normalizeExpression(expression);
        if (expression.equals(expressionTextField.getText())) return;

        expressionTextField.setText(expression);
        markModified();
    }

    private static String normalizeExpression(String expression) {
        return expression.replaceAll("\\s+", " ").trim();
    }

    @Override
    public Component[] getAlignableComponents() {
        return new Component[] {objectTypeLabel};
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public ObjectFilter<?> getFilter() {
        return Failsafe.nn(filter);
    }

    public boolean isActive() {
        return getFilter().isActive();
    }

    @NotNull
    public ObjectFilterSettingsForm getParentForm() {
        return ensureParentComponent();
    }

    public void remove() {
        markModified();
        getParentForm().removeFilterPanel(getFilter());
    }

    public void setActive(boolean active) {
        ObjectFilter<?> filter = getFilter();
        if (filter.isActive() == active) return;
        filter.setActive(active);

        markModified();
        updateEditorField();
    }

    private void markModified() {
        ObjectFilter<?> filter = getFilter();
        ObjectFilterSettingsForm settingsForm = getParentForm();
        settingsForm.markModified(filter);
        settingsForm.getConfiguration().setModified(true);
    }

    private void updateEditorField() {
        boolean active = getFilter().isActive();
        expressionTextField.setEnabled(active);
        EditorEx editor = (EditorEx) expressionTextField.getEditor();
        if (editor == null) return;

        editor.setBackgroundColor(active ?
                Colors.getTextFieldBackground() :
                Colors.getTextFieldDisabledBackground());
    }
}
