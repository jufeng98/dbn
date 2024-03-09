package com.dbn.object.filter.custom.ui;

import com.dbn.common.color.Colors;
import com.dbn.common.expression.ExpressionEvaluator;
import com.dbn.common.expression.ExpressionEvaluatorContext;
import com.dbn.common.icon.Icons;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.form.DBNHeaderForm;
import com.dbn.common.ui.util.Borders;
import com.dbn.common.util.Documents;
import com.dbn.common.util.Editors;
import com.dbn.connection.ConnectionHandler;
import com.dbn.language.sql.SQLFileType;
import com.dbn.language.sql.SQLLanguage;
import com.dbn.object.filter.custom.ObjectFilter;
import com.dbn.object.type.DBObjectType;
import com.dbn.vfs.DatabaseFileViewProvider;
import com.dbn.vfs.file.DBObjectFilterExpressionFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.dbn.common.util.Commons.nvl;

public class ObjectFilterDetailsForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel editorPanel;
    private JLabel errorLabel;
    private JLabel expressionLabel;

    private final ObjectFilter filter;
    private Document document;
    private EditorEx editor;
    private String expression;

    public ObjectFilterDetailsForm(ObjectFilterDetailsDialog parent) {
        super(parent);

        filter = parent.getFilter();
        initHeaderPanel();
        initExpressionEditor();
        initErrorLabel(null, null);
    }

    @Override
    protected JComponent getMainComponent() {
        return mainPanel;
    }

    private void initErrorLabel(String error, String expression) {
        if (error == null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText(error);
            errorLabel.setIcon(Icons.EXEC_MESSAGES_ERROR);
        }
        expressionLabel.setText(nvl(expression, "").replaceAll("\\n", " "));
    }

    private void initHeaderPanel() {
        DBObjectType objectType = filter.getObjectType();
        ConnectionHandler connection = filter.getSettings().getConnection();

        JBColor color = connection.getEnvironmentType().getColor();
        Icon icon = objectType.getIcon();
        String title = objectType.getName().toUpperCase();

        DBNHeaderForm headerForm = new DBNHeaderForm(this, title, icon, color);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
    }

    private void initExpressionEditor() {
        Project project = filter.getProject();
        DBObjectFilterExpressionFile expressionFile = new DBObjectFilterExpressionFile(filter);
        DatabaseFileViewProvider viewProvider = new DatabaseFileViewProvider(project, expressionFile, true);
        PsiFile selectStatementFile = expressionFile.initializePsiFile(viewProvider, SQLLanguage.INSTANCE);

        document = Documents.ensureDocument(selectStatementFile);
        editor = Editors.createEditor(document, project, expressionFile, SQLFileType.INSTANCE);
        Editors.initEditorHighlighter(editor, SQLLanguage.INSTANCE, filter.getConnection());

        editor.setEmbeddedIntoDialogWrapper(true);
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                expression = event.getDocument().getText();
                verifyExpression();
            }
        });

        JScrollPane editorScrollPane = editor.getScrollPane();
        editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        editorScrollPane.setViewportBorder(Borders.lineBorder(Colors.getEditorBackground(), 4));

        EditorSettings settings = editor.getSettings();
        settings.setFoldingOutlineShown(false);
        settings.setLineMarkerAreaShown(false);
        settings.setLineNumbersShown(false);
        settings.setVirtualSpace(false);
        settings.setDndEnabled(false);
        settings.setAdditionalLinesCount(2);
        settings.setRightMarginShown(false);
        settings.setUseTabCharacter(true);

        editorPanel.add(editor.getComponent(), BorderLayout.CENTER);
    }

    private void verifyExpression() {
        ExpressionEvaluator expressionEvaluator = filter.getSettings().getExpressionEvaluator();

        ExpressionEvaluatorContext evaluatorContext = filter.createTestEvaluationContext();

        boolean valid = expressionEvaluator.isValidExpression(expression, Boolean.class, evaluatorContext);
        initErrorLabel(valid ? null : "Invalid expression", evaluatorContext.getEvaluatedExpression());

    }


    public void disposeInner() {
        Editors.releaseEditor(editor);
        editor = null;
        document = null;
        super.disposeInner();
    }
}
