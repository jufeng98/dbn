package com.dbn.data.find.action;

import com.dbn.common.action.ToggleAction;
import com.dbn.common.ui.misc.DBNCheckboxAction;
import com.dbn.data.find.DataSearchComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class DataSearchHeaderToggleAction extends ToggleAction implements DumbAware {
    private final DataSearchComponent searchComponent;

    protected DataSearchHeaderToggleAction(DataSearchComponent searchComponent, String text, Icon icon, Icon hoveredIcon, Icon selectedIcon) {
        super(text);
        this.searchComponent = searchComponent;
        Presentation templatePresentation = getTemplatePresentation();
        templatePresentation.setIcon(icon);
        templatePresentation.setHoveredIcon(hoveredIcon);
        templatePresentation.setSelectedIcon(selectedIcon);
    }

    public DataSearchComponent getEditorSearchComponent() {
        return searchComponent;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }

//    @NotNull
//    @Override
//    public JComponent createCustomComponent(@NotNull Presentation presentation, @NotNull String place) {
//        final JComponent customComponent = super.createCustomComponent(presentation, place);
//        if (customComponent instanceof JCheckBox) {
//            JCheckBox checkBox = (JCheckBox) customComponent;
//            checkBox.setFocusable(false);
//            checkBox.setOpaque(false);
//        }
//        return customComponent;
//    }
}
