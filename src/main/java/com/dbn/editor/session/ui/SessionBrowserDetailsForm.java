package com.dbn.editor.session.ui;

import com.dbn.common.icon.Icons;
import com.dbn.common.ref.WeakRef;
import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.DatabaseFeature;
import com.dbn.editor.session.SessionBrowser;
import com.dbn.editor.session.details.SessionDetailsTable;
import com.dbn.editor.session.details.SessionDetailsTableModel;
import com.dbn.editor.session.model.SessionBrowserModelRow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.JBTabsFactory;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class SessionBrowserDetailsForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel sessionDetailsTabsPanel;
    private JBScrollPane sessionDetailsTablePane;
    private final SessionDetailsTable sessionDetailsTable;

    private final WeakRef<SessionBrowser> sessionBrowser;
    @Getter
    private final SessionBrowserCurrentSqlPanel currentSqlPanel;

    public SessionBrowserDetailsForm(@NotNull DBNComponent parent, SessionBrowser sessionBrowser) {
        super(parent);
        this.sessionBrowser = WeakRef.of(sessionBrowser);
        sessionDetailsTable = new SessionDetailsTable(this);
        sessionDetailsTablePane.setViewportView(sessionDetailsTable);

        JBTabs detailsTabbedPane = JBTabsFactory.createTabs(getProject(), parent);
        sessionDetailsTabsPanel.add((Component) detailsTabbedPane, BorderLayout.CENTER);

        currentSqlPanel = new SessionBrowserCurrentSqlPanel(this, sessionBrowser);
        TabInfo currentSqlTabInfo = new TabInfo(currentSqlPanel.getComponent());
        currentSqlTabInfo.setText("Current Statement");
        currentSqlTabInfo.setIcon(Icons.FILE_SQL_CONSOLE);
        currentSqlTabInfo.setObject(currentSqlPanel);
        detailsTabbedPane.addTab(currentSqlTabInfo);

        ConnectionHandler connection = getConnection();
        if (DatabaseFeature.EXPLAIN_PLAN.isSupported(connection)) {
            TabInfo explainPlanTabInfo = new TabInfo(new JPanel());
            explainPlanTabInfo.setText("Explain Plan");
            explainPlanTabInfo.setIcon(Icons.EXPLAIN_PLAN_RESULT);
            //explainPlanTabInfo.setObject(currentSqlPanel);
            detailsTabbedPane.addTab(explainPlanTabInfo);
        }

        detailsTabbedPane.addListener(new TabsListener() {
            @Override
            public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
                //noinspection StatementWithEmptyBody
                if (newSelection.getText().equals("Explain Plan")) {

                }
            }
        });
    }

    @NotNull
    private ConnectionHandler getConnection() {
        return getSessionBrowser().getConnection();
    }

    @NotNull
    public SessionBrowser getSessionBrowser() {
        return sessionBrowser.ensure();
    }

    public void update(@Nullable final SessionBrowserModelRow selectedRow) {
        SessionDetailsTableModel model = new SessionDetailsTableModel(selectedRow);
        sessionDetailsTable.setModel(model);
        sessionDetailsTable.accommodateColumnsSize();
        currentSqlPanel.loadCurrentStatement();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
