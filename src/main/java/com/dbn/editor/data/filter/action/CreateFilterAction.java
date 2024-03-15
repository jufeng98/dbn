package com.dbn.editor.data.filter.action;

import com.dbn.common.action.ProjectActionGroup;
import com.dbn.common.icon.Icons;
import com.dbn.editor.data.filter.ui.DatasetFilterList;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

public class CreateFilterAction extends ProjectActionGroup {
    private DatasetFilterList filterList;

    public CreateFilterAction(DatasetFilterList filterList) {
        this.filterList = filterList;
    }

    @Override
    public AnAction[] getChildren(AnActionEvent e) {
        return new AnAction[]{
            new CreateBasicFilterAction(filterList),
            new CreateCustomFilterAction(filterList)
        };
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Create Filter");
        presentation.setIcon(Icons.ACTION_ADD);
    }
}
