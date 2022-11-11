package com.dci.intellij.dbn.execution.method.result.ui;

import com.dci.intellij.dbn.common.action.DataKeys;
import com.dci.intellij.dbn.common.action.DataProviders;
import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.util.UserInterface;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.data.find.DataSearchComponent;
import com.dci.intellij.dbn.data.find.SearchableDataComponent;
import com.dci.intellij.dbn.data.grid.ui.table.basic.BasicTableScrollPane;
import com.dci.intellij.dbn.data.grid.ui.table.resultSet.ResultSetTable;
import com.dci.intellij.dbn.data.model.resultSet.ResultSetDataModel;
import com.dci.intellij.dbn.data.record.RecordViewInfo;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.object.DBArgument;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.IdeBorderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MethodExecutionCursorResultForm extends DBNFormBase implements SearchableDataComponent {
    private JPanel actionsPanel;
    private JScrollPane resultScrollPane;
    private JPanel mainPanel;
    private JPanel resultPanel;
    private JPanel searchPanel;

    private final DBObjectRef<DBArgument> argumentRef;
    private final ResultSetTable<ResultSetDataModel<?, ?>> resultTable;

    private final Latent<DataSearchComponent> dataSearchComponent = Latent.basic(() -> {
        DataSearchComponent dataSearchComponent = new DataSearchComponent(MethodExecutionCursorResultForm.this);
        searchPanel.add(dataSearchComponent.getComponent(), BorderLayout.CENTER);
        DataProviders.register(dataSearchComponent.getSearchField(), this);
        return dataSearchComponent;
    });

    MethodExecutionCursorResultForm(MethodExecutionResultForm parent, MethodExecutionResult executionResult, DBArgument argument) {
        super(parent);
        this.argumentRef = DBObjectRef.of(argument);
        ResultSetDataModel<?, ?> dataModel = executionResult.getTableModel(argument);
        RecordViewInfo recordViewInfo = new RecordViewInfo(
                executionResult.getName(),
                executionResult.getIcon());

        resultTable = new ResultSetTable<>(this, dataModel, true, recordViewInfo);
        resultTable.setPreferredScrollableViewportSize(new Dimension(500, -1));

        resultPanel.setBorder(IdeBorderFactory.createBorder());
        resultScrollPane.setViewportView(resultTable);
        resultScrollPane.getViewport().setBackground(Colors.getTableBackground());
        resultTable.initTableGutter();

        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "", true, "DBNavigator.ActionGroup.MethodExecutionCursorResult");
        actionsPanel.add(actionToolbar.getComponent());
        DataProviders.register(actionToolbar.getComponent(), this);
    }

    public DBArgument getArgument() {
        return argumentRef.get();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    private void createUIComponents() {
        resultScrollPane = new BasicTableScrollPane();
    }

    /*********************************************************
     *              SearchableDataComponent                  *
     *********************************************************/
    @Override
    public void showSearchHeader() {
        resultTable.clearSelection();

        DataSearchComponent dataSearchComponent = getSearchComponent();
        dataSearchComponent.initializeFindModel();
        JTextField searchField = dataSearchComponent.getSearchField();
        if (searchPanel.isVisible()) {
            searchField.selectAll();
        } else {
            searchPanel.setVisible(true);
        }
        searchField.requestFocus();

    }

    private DataSearchComponent getSearchComponent() {
        return dataSearchComponent.get();
    }

    @Override
    public void hideSearchHeader() {
        getSearchComponent().resetFindModel();
        searchPanel.setVisible(false);
        UserInterface.repaintAndFocus(resultTable);
    }

    @Override
    public void cancelEditActions() {

    }

    @Override
    public String getSelectedText() {
        return null;
    }

    @NotNull
    @Override
    public ResultSetTable<?> getTable() {
        return Failsafe.nn(resultTable);
    }

    /********************************************************
     *                    Data Provider                     *
     ********************************************************/
    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        if (DataKeys.METHOD_EXECUTION_CURSOR_RESULT_FORM.is(dataId)) {
            return MethodExecutionCursorResultForm.this;
        }
        if (DataKeys.METHOD_EXECUTION_ARGUMENT.is(dataId)) {
            return DBObjectRef.get(argumentRef);
        }
        return super.getData(dataId);
    }
}