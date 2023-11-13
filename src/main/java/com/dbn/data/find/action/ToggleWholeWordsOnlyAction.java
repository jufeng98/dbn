package com.dbn.data.find.action;

import com.dbn.data.find.DataSearchComponent;
import com.intellij.find.FindSettings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ToggleWholeWordsOnlyAction extends DataSearchHeaderToggleAction {
    public ToggleWholeWordsOnlyAction(DataSearchComponent searchComponent) {
        super(searchComponent, "W&hole Words");
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return getEditorSearchComponent().getFindModel().isWholeWordsOnly();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(!getEditorSearchComponent().getFindModel().isRegularExpressions());
        e.getPresentation().setVisible(true);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        FindSettings.getInstance().setLocalWholeWordsOnly(state);
        getEditorSearchComponent().getFindModel().setWholeWordsOnly(state);
    }
}
