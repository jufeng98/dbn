package com.dbn.diagnostics.action;

import com.dbn.common.ui.misc.DBNComboBoxAction;
import com.dbn.common.util.Strings;
import com.dbn.diagnostics.ParserDiagnosticsManager;
import com.dbn.diagnostics.data.ParserDiagnosticsFilter;
import com.dbn.diagnostics.ui.ParserDiagnosticsForm;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;

public class ParserDiagnosticsFileTypeFilterAction extends DBNComboBoxAction implements DumbAware {
    private final ParserDiagnosticsForm form;

    public ParserDiagnosticsFileTypeFilterAction(ParserDiagnosticsForm form) {
        this.form = form;
    }

    @Override
    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent component) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new SelectFilterValueAction(null));
        actionGroup.addSeparator();
        ParserDiagnosticsManager manager = form.getManager();
        String[] fileExtensions = manager.getFileExtensions();
        for (String fileType : fileExtensions) {
            actionGroup.add(new SelectFilterValueAction(fileType));
        }
        return actionGroup;
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();

        ParserDiagnosticsFilter resultFilter = getResultFilter();
        String fileType = resultFilter.getFileType();
        presentation.setText(Strings.isEmpty(fileType) ? "file type" : "*." + fileType, false);
    }

    private ParserDiagnosticsFilter getResultFilter() {
        return form.getManager().getResultFilter();
    }

    private class SelectFilterValueAction extends AnAction {
        private final String fileType;

        public SelectFilterValueAction(String fileType) {
            super(Strings.isEmpty(fileType) ? "No Filter" : "*." + fileType);
            this.fileType = fileType;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            getResultFilter().setFileType(fileType);
            form.refreshResult();
        }
    }
 }