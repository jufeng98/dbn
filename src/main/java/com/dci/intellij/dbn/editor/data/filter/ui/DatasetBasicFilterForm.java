package com.dci.intellij.dbn.editor.data.filter.ui;

import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.dispose.DisposableContainers;
import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.ui.ValueSelector;
import com.dci.intellij.dbn.common.ui.ValueSelectorOption;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.ui.util.UserInterface;
import com.dci.intellij.dbn.common.util.Documents;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.common.util.Viewers;
import com.dci.intellij.dbn.editor.data.filter.ConditionJoinType;
import com.dci.intellij.dbn.editor.data.filter.ConditionOperator;
import com.dci.intellij.dbn.editor.data.filter.DatasetBasicFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetBasicFilterCondition;
import com.dci.intellij.dbn.language.sql.SQLFileType;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.vfs.DatabaseFileViewProvider;
import com.dci.intellij.dbn.vfs.file.DBDatasetFilterVirtualFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dci.intellij.dbn.common.ui.util.ComboBoxes.*;

public class DatasetBasicFilterForm extends ConfigurationEditorForm<DatasetBasicFilter> {
    private JPanel conditionsPanel;
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JTextField nameTextField;
    private JLabel errorLabel;
    private JPanel previewPanel;
    private JPanel addConditionsPanel;
    private JPanel filterNamePanel;
    private JComboBox<ConditionJoinType> joinTypeComboBox;

    private final DBObjectRef<DBDataset> datasetRef;
    private final List<DatasetBasicFilterConditionForm> conditionForms = DisposableContainers.list(this);
    private Document previewDocument;
    private boolean isCustomNamed;
    private EditorEx viewer;


    public DatasetBasicFilterForm(DBDataset dataset, DatasetBasicFilter filter) {
        super(filter);
        conditionsPanel.setLayout(new BoxLayout(conditionsPanel, BoxLayout.Y_AXIS));
        datasetRef = DBObjectRef.of(dataset);
        nameTextField.setText(filter.getDisplayName());

        actionsPanel.add(new ColumnSelector(), BorderLayout.CENTER);
        addConditionsPanel.setBorder(Borders.BOTTOM_LINE_BORDER);
        filterNamePanel.setBorder(Borders.BOTTOM_LINE_BORDER);

        for (DatasetBasicFilterCondition condition : filter.getConditions()) {
            addConditionPanel(condition);
        }

        initComboBox(joinTypeComboBox, ConditionJoinType.values());
        setSelection(joinTypeComboBox, filter.getJoinType());

        nameTextField.addKeyListener(createKeyListener());
        registerComponent(mainPanel);

        if (filter.getError() == null) {
            errorLabel.setText("");
        } else {
            errorLabel.setText(filter.getError());
            errorLabel.setIcon(Icons.EXEC_MESSAGES_ERROR);
        }
        updateNameAndPreview();
        isCustomNamed = filter.isCustomNamed();
    }

    private class ColumnSelector extends ValueSelector<DBColumn> {
        ColumnSelector() {
            super(PlatformIcons.ADD_ICON, "Add Condition", null, ValueSelectorOption.HIDE_DESCRIPTION);
            addListener((oldValue, newValue) -> addConditionPanel(newValue));
        }

        @Override
        public List<DBColumn> loadValues() {
            DBDataset dataset = getDataset();
            List<DBColumn> columns = new ArrayList<>(dataset.getColumns());
            Collections.sort(columns);
            return columns;
        }
    }

    @Override
    public void focus() {
        if (conditionForms.size() > 0) {
            conditionForms.get(0).focus();
        }
    }

    @Override
    protected ActionListener createActionListener() {
        return e -> updateNameAndPreview();
    }

    private KeyListener createKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                isCustomNamed = true;
                nameTextField.setForeground(Colors.getTextFieldForeground());
            }
        };
    }


    private void updateGeneratedName() {
        if (!isDisposed() && (!isCustomNamed || nameTextField.getText().trim().length() == 0)) {
            getConfiguration().setCustomNamed(false);
            boolean addSeparator = false;
            StringBuilder buffer = new StringBuilder();
            for (DatasetBasicFilterConditionForm conditionForm : conditionForms) {
                if (conditionForm.isActive()) {
                    if (addSeparator) buffer.append(getSelection(joinTypeComboBox) == ConditionJoinType.AND ? " & " : " | ");
                    addSeparator = true;
                    buffer.append(conditionForm.getValue());
                    if (buffer.length() > 40) {
                        buffer.setLength(40);
                        buffer.append("...");
                        break;
                    }
                }
            }

            String name = buffer.length() > 0 ? buffer.toString() : getConfiguration().getFilterGroup().createFilterName("Filter");
            nameTextField.setText(name);
            nameTextField.setForeground(UIUtil.getInactiveTextColor());
        }
    }

    public void updateNameAndPreview() {
        DBDataset dataset = this.getDataset();
        if (dataset == null) return;

        updateGeneratedName();
        StringBuilder selectStatement = new StringBuilder("select * from ");
        selectStatement.append(dataset.getSchema().getQuotedName(false)).append('.');
        selectStatement.append(dataset.getQuotedName(false));
        selectStatement.append(" where\n    ");

        boolean addJoin = false;
        for (DatasetBasicFilterConditionForm conditionForm : conditionForms) {
            DatasetBasicFilterCondition condition = conditionForm.getCondition();
            if (conditionForm.isActive()) {
                if (addJoin) {
                    selectStatement.append(getSelection(joinTypeComboBox) == ConditionJoinType.AND ? " and\n    " : " or\n    ");
                }
                addJoin = true;
                condition.appendConditionString(selectStatement, dataset);
            }
        }

        if (previewDocument == null) {
            Project project = dataset.getProject();
            DBDatasetFilterVirtualFile filterFile = new DBDatasetFilterVirtualFile(dataset, selectStatement.toString());
            DatabaseFileViewProvider viewProvider = new DatabaseFileViewProvider(project, filterFile, true);
            PsiFile selectStatementFile = filterFile.initializePsiFile(viewProvider, SQLLanguage.INSTANCE);

            previewDocument = Documents.ensureDocument(selectStatementFile);

            this.viewer = Viewers.createViewer(previewDocument, project, filterFile, SQLFileType.INSTANCE);
            this.viewer.setEmbeddedIntoDialogWrapper(true);

            Editors.initEditorHighlighter(this.viewer, SQLLanguage.INSTANCE, dataset);
            Editors.setEditorReadonly(this.viewer, true);

            JScrollPane viewerScrollPane = this.viewer.getScrollPane();
            viewerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            viewerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            //viewerScrollPane.setBorder(null);
            viewerScrollPane.setViewportBorder(Borders.lineBorder(Colors.getReadonlyEditorBackground(), 4));

            EditorSettings settings = this.viewer.getSettings();
            settings.setFoldingOutlineShown(false);
            settings.setLineMarkerAreaShown(false);
            settings.setLineNumbersShown(false);
            settings.setVirtualSpace(false);
            settings.setDndEnabled(false);
            settings.setAdditionalLinesCount(2);
            settings.setRightMarginShown(false);
            this.viewer.getComponent().setFocusable(false);
            previewPanel.add(this.viewer.getComponent(), BorderLayout.CENTER);

        } else {
            Documents.setText(previewDocument, selectStatement);
        }

    }

    public String getFilterName() {
        return nameTextField.getText();
    }

    public DBDataset getDataset() {
        return datasetRef.get();
    }

    private void addConditionPanel(DatasetBasicFilterCondition condition) {
        condition.createComponent();
        DatasetBasicFilterConditionForm conditionForm = condition.getSettingsEditor();
        if (conditionForm != null) {
            conditionForm.setBasicFilterPanel(this);
            conditionForms.add(conditionForm);
            conditionsPanel.add(conditionForm.getComponent());

            UserInterface.repaint(conditionsPanel);
            conditionForm.focus();
        }
    }

    public void addConditionPanel(DBColumn column) {
        DatasetBasicFilter filter = getConfiguration();
        DatasetBasicFilterCondition condition = new DatasetBasicFilterCondition(filter);
        condition.setColumnName(column == null ? null : column.getName());
        condition.setOperator(ConditionOperator.EQUAL);
        addConditionPanel(condition);
        updateNameAndPreview();
    }

    void removeConditionPanel(DatasetBasicFilterConditionForm conditionForm) {
        conditionForms.remove(conditionForm);
        conditionsPanel.remove(conditionForm.getComponent());
        Disposer.dispose(conditionForm);
        UserInterface.repaint(conditionsPanel);
        updateNameAndPreview();
    }


    /*************************************************
     *                  SettingsEditor               *
     *************************************************/

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        updateGeneratedName();
        DatasetBasicFilter filter = getConfiguration();
        filter.setJoinType(getSelection(joinTypeComboBox));
        filter.setCustomNamed(isCustomNamed);
        filter.getConditions().clear();
        for (DatasetBasicFilterConditionForm conditionForm : conditionForms) {
            conditionForm.applyFormChanges();
            filter.addCondition(conditionForm.getConfiguration());
        }
        filter.setName(nameTextField.getText());
    }

    @Override
    public void resetFormChanges() {

    }

    @Override
    public void disposeInner() {
        Editors.releaseEditor(viewer);
        super.disposeInner();
    }
}
