package com.dci.intellij.dbn.editor.session.ui;

import com.dci.intellij.dbn.common.action.DataProviders;
import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.misc.DBNScrollPane;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.ui.util.UserInterface;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.find.DataSearchComponent;
import com.dci.intellij.dbn.data.find.SearchableDataComponent;
import com.dci.intellij.dbn.data.grid.ui.table.basic.BasicTable;
import com.dci.intellij.dbn.editor.data.ui.table.cell.DatasetTableCellEditor;
import com.dci.intellij.dbn.editor.session.SessionBrowser;
import com.dci.intellij.dbn.editor.session.model.SessionBrowserModel;
import com.dci.intellij.dbn.editor.session.ui.table.SessionBrowserTable;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.util.ui.AsyncProcessIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class SessionBrowserForm extends DBNFormBase implements SearchableDataComponent {
    private JPanel actionsPanel;
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel loadingIconPanel;
    private JPanel detailsPanel;
    private JPanel editorPanel;
    private JLabel loadingLabel;
    private JLabel loadTimestampLabel;
    private DBNScrollPane tableScrollPane;
    private SessionBrowserTable browserTable;

    private final Latent<DataSearchComponent> dataSearchComponent = Latent.basic(() -> {
        DataSearchComponent dataSearchComponent = new DataSearchComponent(SessionBrowserForm.this);
        searchPanel.add(dataSearchComponent.getComponent(), BorderLayout.CENTER);
        DataProviders.register(dataSearchComponent.getSearchField(), this);
        return dataSearchComponent;
    });

    private final WeakRef<SessionBrowser> sessionBrowser;
    private final SessionBrowserDetailsForm detailsForm;

    public SessionBrowserForm(SessionBrowser sessionBrowser) {
        super(sessionBrowser, sessionBrowser.getProject());
        this.sessionBrowser = WeakRef.of(sessionBrowser);
        editorPanel.setBorder(Borders.tableBorder(1, 0, 0, 0));
        browserTable = new SessionBrowserTable(this, sessionBrowser);
        tableScrollPane.setViewportView(browserTable);
        browserTable.initTableGutter();
        detailsForm = new SessionBrowserDetailsForm(this, sessionBrowser);
        detailsPanel.add(detailsForm.getComponent(), BorderLayout.CENTER);

        loadTimestampLabel.setForeground(Colors.HINT_COLOR);
        refreshLoadTimestamp();

        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.SessionBrowser", "", true);

        actionsPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);
        loadingIconPanel.add(new AsyncProcessIcon("Loading"), BorderLayout.CENTER);
        hideLoadingHint();

        DataProviders.register(actionsPanel, this);
        Disposer.register(this, browserTable);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public SessionBrowserDetailsForm getDetailsForm() {
        return detailsForm;
    }

    public void showLoadingHint() {
        Dispatch.run(() -> {
            Failsafe.nd(this);
            loadingLabel.setVisible(true);
            loadingIconPanel.setVisible(true);
            loadTimestampLabel.setVisible(false);
            refreshLoadTimestamp();
        });
    }

    public void hideLoadingHint() {
        Dispatch.run(() -> {
            Failsafe.nd(this);
            loadingLabel.setVisible(false);
            loadingIconPanel.setVisible(false);
            refreshLoadTimestamp();
        });
    }

    public void refreshLoadTimestamp() {
        boolean visible = !loadingLabel.isVisible();
        if (visible) {
            SessionBrowserModel model = getBrowserTable().getModel();
            long timestamp = model.getTimestamp();
/*
            RegionalSettings regionalSettings = RegionalSettings.getInstance(sessionBrowser.getProject());
            String dateTime = regionalSettings.getFormatter().formatTime(new Date(timestamp));
            loadTimestampLabel.setText("Updated: " + dateTime + " (" + DateFormatUtil.formatPrettyDateTime(timestamp)+ ")");
*/

            loadTimestampLabel.setText("Updated: " + DateFormatUtil.formatPrettyDateTime(timestamp));
        }
        loadTimestampLabel.setVisible(visible);
    }


    @NotNull
    public SessionBrowserTable getBrowserTable() {
        return Failsafe.nn(browserTable);
    }

    @NotNull
    public SessionBrowser getSessionBrowser() {
        return sessionBrowser.ensure();
    }

    @NotNull
    private ConnectionHandler getConnectionHandler() {
        return getSessionBrowser().getConnection();
    }


    /*********************************************************
     *              SearchableDataComponent                  *
     *********************************************************/
    @Override
    public void showSearchHeader() {
        getBrowserTable().clearSelection();

        DataSearchComponent dataSearchComponent = getSearchComponent();
        dataSearchComponent.initializeFindModel();
        JTextField searchField = dataSearchComponent.getSearchField();
        if (searchPanel.isVisible()) {
            searchField.selectAll();
        } else {
            searchPanel.setVisible(true);    
        }
        searchField.requestFocus();

    }

    private DataSearchComponent getSearchComponent() {
        return dataSearchComponent.get();
    }

    @Override
    public void hideSearchHeader() {
        getSearchComponent().resetFindModel();
        searchPanel.setVisible(false);
        SessionBrowserTable editorTable = getBrowserTable();
        UserInterface.repaintAndFocus(editorTable);
    }

    @Override
    public void cancelEditActions() {}

    @Override
    public String getSelectedText() {
        TableCellEditor cellEditor = getBrowserTable().getCellEditor();
        if (cellEditor instanceof DatasetTableCellEditor) {
            DatasetTableCellEditor tableCellEditor = (DatasetTableCellEditor) cellEditor;
            return tableCellEditor.getTextField().getSelectedText();
        }
        return null;
    }

    @NotNull
    @Override
    public BasicTable getTable() {
        return getBrowserTable();
    }

    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        Object data = super.getData(dataId);
        if (data == null) {
            data = getSessionBrowser().getData(dataId);
        }
        return data;
    }

    @Override
    public void disposeInner() {
        DataManager.removeDataProvider(actionsPanel);
        super.disposeInner();
        browserTable = null;
    }
}
