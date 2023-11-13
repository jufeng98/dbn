package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.misc.DBNComboBoxAction;
import com.dci.intellij.dbn.common.util.Context;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterGroup;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class DatasetFiltersSelectAction extends DBNComboBoxAction {
    public DatasetFiltersSelectAction() {
        Presentation presentation = getTemplatePresentation();
        presentation.setText("No Filter");
        presentation.setIcon(Icons.DATASET_FILTER_EMPTY);
    }

    @NotNull
    @Override
    public JComponent createCustomComponent(@NotNull Presentation presentation) {
        return super.createCustomComponent(presentation);
    }



    @Override
    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        DataContext dataContext = Context.getDataContext(button);
        DatasetEditor datasetEditor = DatasetEditor.get(dataContext);

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        if (datasetEditor != null) {
            DBDataset dataset = datasetEditor.getDataset();
            DatasetFilterOpenAction datasetFilterOpenAction = new DatasetFilterOpenAction(datasetEditor);
            datasetFilterOpenAction.setInjectedContext(true);
            actionGroup.add(datasetFilterOpenAction);
            actionGroup.addSeparator();
            actionGroup.add(new DatasetFilterSelectAction(dataset, DatasetFilterManager.EMPTY_FILTER));
            actionGroup.addSeparator();

            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            DatasetFilterGroup filterGroup = filterManager.getFilterGroup(dataset);
            for (DatasetFilter filter : filterGroup.getFilters()) {
                actionGroup.add(new DatasetFilterSelectAction(dataset, filter));
            }
        }
        return actionGroup;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        DatasetEditor datasetEditor = DatasetEditor.get(e);

        Presentation presentation = e.getPresentation();
        boolean enabled =
                isValid(datasetEditor) &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading();

        if (isValid(datasetEditor)) {
            DBDataset dataset = datasetEditor.getDataset();

            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            DatasetFilter activeFilter = filterManager.getActiveFilter(dataset);

            if (activeFilter == null) {
                presentation.setText("No Filter");
                presentation.setIcon(Icons.DATASET_FILTER_EMPTY);
            } else {
                //e.getPresentation().setText(activeFilter.getName());
                presentation.setText(activeFilter.getName(), false);
                presentation.setIcon(activeFilter.getIcon());
            }
        }

        //if (!enabled) presentation.setIcon(null);
        presentation.setEnabled(enabled);
    }
}
