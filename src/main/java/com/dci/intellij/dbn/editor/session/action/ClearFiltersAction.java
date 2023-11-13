package com.dci.intellij.dbn.editor.session.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.session.SessionBrowser;
import com.dci.intellij.dbn.editor.session.SessionBrowserFilter;
import com.dci.intellij.dbn.editor.session.model.SessionBrowserModel;
import com.dci.intellij.dbn.editor.session.options.SessionBrowserSettings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

public class ClearFiltersAction extends AbstractSessionBrowserAction {

    public ClearFiltersAction() {
        super("Clear Filter", Icons.DATASET_FILTER_CLEAR);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        if (sessionBrowser != null) {
            sessionBrowser.clearFilter();
            SessionBrowserSettings sessionBrowserSettings = sessionBrowser.getSettings();
            if (sessionBrowserSettings.isReloadOnFilterChange()) {
                sessionBrowser.loadSessions(false);
            } else {
                sessionBrowser.refreshTable();
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setText("Clear Filter");

        boolean enabled = false;
        SessionBrowser sessionBrowser = getSessionBrowser(e);
        if (sessionBrowser != null) {
            SessionBrowserModel tableModel = sessionBrowser.getTableModel();
            if (tableModel != null) {
                SessionBrowserFilter filter = tableModel.getFilter();
                if (filter != null) {
                    enabled = !filter.isEmpty();
                }
            }
        }

        presentation.setEnabled(enabled);

    }
}