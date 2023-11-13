package com.dbn.browser.ui;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.environment.EnvironmentType;
import com.dbn.common.environment.options.EnvironmentVisibilitySettings;
import com.dbn.common.environment.options.listener.EnvironmentManagerListener;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.ui.tab.TabbedPane;
import com.dbn.common.util.Commons;
import com.dbn.connection.ConnectionBundle;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.ConnectionManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.dbn.common.dispose.Failsafe.guarded;

public class TabbedBrowserForm extends DatabaseBrowserForm{
    private final TabbedPane connectionTabs;
    private JPanel mainPanel;

    TabbedBrowserForm(@NotNull BrowserToolWindowForm parent, @Nullable TabbedBrowserForm previous) {
        super(parent);
        connectionTabs = new TabbedPane(this);
        //connectionTabs.setSingleRow(false);
        connectionTabs.setHideTabs(false);
        connectionTabs.setAutoscrolls(true);
        //connectionTabs.setBackground(GUIUtil.getListBackground());
        //mainPanel.add(connectionTabs, BorderLayout.CENTER);
        initTabs(previous);
        ProjectEvents.subscribe(getProject(), this, EnvironmentManagerListener.TOPIC, environmentManagerListener());
    }

    @NotNull
    private EnvironmentManagerListener environmentManagerListener() {
        return new EnvironmentManagerListener() {
            @Override
            public void configurationChanged(Project project) {
                EnvironmentVisibilitySettings visibilitySettings = getEnvironmentSettings(project).getVisibilitySettings();
                for (TabInfo tabInfo : listTabs()) {
                    guarded(tabInfo, ti -> {
                        SimpleBrowserForm browserForm = (SimpleBrowserForm) ti.getObject();
                        ConnectionHandler connection = browserForm.getConnection();
                        if (connection == null) return;

                        JBColor environmentColor = connection.getEnvironmentType().getColor();
                        if (visibilitySettings.getConnectionTabs().value()) {
                            ti.setTabColor(environmentColor);
                        } else {
                            ti.setTabColor(null);
                        }
                    });
                }
            }
        };
    }



    private void initTabs(@Nullable TabbedBrowserForm previous) {
        Project project = ensureProject();

        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
        for (ConnectionHandler connection: connectionBundle.getConnections()) {
            ConnectionId connectionId = connection.getConnectionId();
            SimpleBrowserForm browserForm = previous == null ? null : previous.removeBrowserForm(connectionId);
            if (browserForm == null) {
                browserForm = new SimpleBrowserForm(this, connection);
            } else {
                browserForm.setParent(this);
            }

            JComponent component = browserForm.getComponent();
            TabInfo tabInfo = new TabInfo(component);
            tabInfo.setText(Commons.nvl(connection.getName(), "[unnamed connection]"));
            tabInfo.setObject(browserForm);
            //tabInfo.setIcon(connection.getIcon());
            this.connectionTabs.addTab(tabInfo);

            EnvironmentType environmentType = connection.getEnvironmentType();
            tabInfo.setTabColor(environmentType.getColor());
        }
        if (this.connectionTabs.getTabCount() == 0) {
            mainPanel.removeAll();
            mainPanel.add(new JBList(new ArrayList()), BorderLayout.CENTER);
        } else {
            if (mainPanel.getComponentCount() > 0) {
                Component component = mainPanel.getComponent(0);
                if (component != this.connectionTabs) {
                    mainPanel.removeAll();
                    mainPanel.add(this.connectionTabs, BorderLayout.CENTER);
                }
            } else {
                mainPanel.add(this.connectionTabs, BorderLayout.CENTER);
            }
        }
    }

    @Nullable
    private SimpleBrowserForm getBrowserForm(ConnectionId connectionId) {
        for (TabInfo tabInfo : listTabs()) {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            ConnectionHandler connection = browserForm.getConnection();
            if (connection != null && connection.getConnectionId() == connectionId) {
                return browserForm;
            }
        }
        return null;
    }

    @Nullable
    private SimpleBrowserForm removeBrowserForm(ConnectionId connectionId) {
        TabbedPane connectionTabs = getConnectionTabs();
        for (TabInfo tabInfo : connectionTabs.getTabs()) {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            ConnectionId tabConnectionId = browserForm.getConnectionId();
            if (tabConnectionId == connectionId) {
                connectionTabs.removeTab(tabInfo, false);
                return browserForm;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    @Nullable
    public DatabaseBrowserTree getBrowserTree() {
        return getActiveBrowserTree();
    }

    @Nullable
    public DatabaseBrowserTree getBrowserTree(ConnectionId connectionId) {
        SimpleBrowserForm browserForm = getBrowserForm(connectionId);
        return browserForm== null ? null : browserForm.getBrowserTree();
    }

    @Nullable
    public DatabaseBrowserTree getActiveBrowserTree() {
        TabInfo tabInfo = getConnectionTabs().getSelectedInfo();
        if (tabInfo != null) {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            return browserForm.getBrowserTree();
        }
        return null;
    }

    @Override
    public void selectElement(BrowserTreeNode treeNode, boolean focus, boolean scroll) {
        ConnectionId connectionId = treeNode.getConnectionId();
        SimpleBrowserForm browserForm = getBrowserForm(connectionId);
        if (browserForm == null) return;

        getConnectionTabs().select(browserForm.getComponent(), focus);
        if (scroll) browserForm.selectElement(treeNode, focus, true);
    }

    @Override
    public void rebuildTree() {
        for (TabInfo tabInfo : listTabs()) {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            browserForm.rebuildTree();
        }
    }

    @NotNull
    public TabbedPane getConnectionTabs() {
        return Failsafe.nn(connectionTabs);
    }

    void refreshTabInfo(ConnectionId connectionId) {
        for (TabInfo tabInfo : listTabs()) {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            ConnectionHandler connection = browserForm.getConnection();
            if (connection != null) {
                if (connection.getConnectionId() == connectionId) {
                    tabInfo.setText(connection.getName());
                    break;
                }
            }
        }

    }

    @NotNull
    private List<TabInfo> listTabs() {
        return new ArrayList<>(getConnectionTabs().getTabs());
    }
}

