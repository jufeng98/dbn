package com.dbn.browser.ui;

import com.dbn.browser.DatabaseBrowserManager;
import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.browser.options.BrowserDisplayMode;
import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.browser.options.listener.DisplayModeSettingsListener;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.common.util.Actions;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.config.ConnectionConfigListener;
import com.dbn.object.common.DBObject;
import com.dbn.object.properties.ui.ObjectPropertiesForm;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.dbn.common.dispose.Checks.isValid;

public class BrowserToolWindowForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel browserPanel;
    private JPanel objectPropertiesPanel;
    private @Getter DatabaseBrowserForm browserForm;

    private BrowserDisplayMode displayMode;
    private final ObjectPropertiesForm objectPropertiesForm;

    public BrowserToolWindowForm(Disposable parent, @NotNull Project project) {
        super(parent, project);
        //toolWindow.setIcon(dbBrowser.getIcon(0));
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        rebuild();

        ActionToolbar actionToolbar = Actions.createActionToolbar(
                actionsPanel,
                "DBNavigator.ActionGroup.Browser.Controls", "",
                true);

        actionsPanel.add(actionToolbar.getComponent());

        objectPropertiesPanel.setVisible(browserManager.getShowObjectProperties().value());
        objectPropertiesForm = new ObjectPropertiesForm(this);
        objectPropertiesPanel.add(objectPropertiesForm.getComponent());


        ProjectEvents.subscribe(project, this, DisplayModeSettingsListener.TOPIC, this::changeDisplayMode);
        ProjectEvents.subscribe(project, this,
                ConnectionConfigListener.TOPIC,
                ConnectionConfigListener
                        .whenSetupChanged(this::rebuild)
                        .whenNameChanged(this::refreshTabs));
    }

    public void rebuild() {
        Project project = ensureProject();
        DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(project);
        displayMode = browserSettings.getGeneralSettings().getDisplayMode();
        DatabaseBrowserForm oldBrowserForm = this.browserForm;

        this.browserForm =
                displayMode == BrowserDisplayMode.TABBED ? new TabbedBrowserForm(this) :
                displayMode == BrowserDisplayMode.SIMPLE ? new SimpleBrowserForm(this) :
                displayMode == BrowserDisplayMode.SELECTOR ? new SelectorBrowserForm(this) : null;

        browserPanel.removeAll();
        browserPanel.add(this.browserForm.getComponent(), BorderLayout.CENTER);
        UserInterface.repaint(browserPanel);

        Disposer.dispose(oldBrowserForm);
    }

    public DatabaseBrowserTree getBrowserTree(ConnectionId connectionId) {
        if (browserForm instanceof TabbedBrowserForm tabbedBrowserForm) {
            return tabbedBrowserForm.getBrowserTree(connectionId);
        }

        if (browserForm instanceof SelectorBrowserForm selectorBrowserForm) {
            return selectorBrowserForm.getBrowserTree(connectionId);
        }

        if (browserForm instanceof SimpleBrowserForm) {
            return browserForm.getBrowserTree();
        }

        return null;
    }



    public void showObjectProperties() {
        Project project = ensureProject();
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        DatabaseBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
        BrowserTreeNode treeNode = activeBrowserTree == null ? null : activeBrowserTree.getSelectedNode();
        if (treeNode instanceof DBObject object) {
            objectPropertiesForm.setObject(object);
        }

        objectPropertiesPanel.setVisible(true);
    }

    public void hideObjectProperties() {
        objectPropertiesPanel.setVisible(false);
    }

    @Nullable
    public DatabaseBrowserTree getActiveBrowserTree() {
        return browserForm.getBrowserTree();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    private void changeDisplayMode(BrowserDisplayMode displayMode) {
        if (this.displayMode != displayMode) {
            this.displayMode = displayMode;
            rebuild();
        }
    }

    private void refreshTabs(ConnectionId connectionId) {
        if (browserForm instanceof TabbedBrowserForm tabbedBrowserForm && isValid(browserForm)) {
            tabbedBrowserForm.refreshTabInfo(connectionId);
        }
    }

    @Override
    public void disposeInner() {
        browserForm = Disposer.replace(browserForm, null);
        super.disposeInner();
    }
}
