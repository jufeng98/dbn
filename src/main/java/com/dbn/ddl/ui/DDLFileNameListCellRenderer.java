package com.dbn.ddl.ui;

import com.dbn.ddl.DDLFileNameProvider;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DDLFileNameListCellRenderer extends ColoredListCellRenderer<DDLFileNameProvider> {
    @Override
    protected void customizeCellRenderer(@NotNull JList list, DDLFileNameProvider value, int index, boolean selected, boolean hasFocus) {

        append(value.getFilePattern(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        append(" (" + value.getDdlFileType().getDescription() + ") ", SimpleTextAttributes.GRAY_ATTRIBUTES);

        //Module module = ProjectRootManager.getInstance(psiFile.getProject()).getFileIndex().getModuleForFile(virtualFile);
        //append(" - module " + module.getName(), SimpleTextAttributes.GRAYED_ATTRIBUTES);

        setIcon(value.getDdlFileType().getLanguageFileType().getIcon());
    }
}