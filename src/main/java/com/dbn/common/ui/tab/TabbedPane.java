package com.dbn.common.ui.tab;

import com.dbn.common.compatibility.Workaround;
import com.dbn.common.dispose.Disposer;
import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.form.DBNForm;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.JBTabsFactory;
import com.intellij.ui.tabs.JBTabsPosition;
import com.intellij.ui.tabs.JBTabsPresentation;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.util.containers.ContainerUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("removal")
@Getter
@Setter
public class TabbedPane extends JComponent implements JBTabs, StatefulDisposable {
    private boolean disposed;
    private JBTabs jbTabs;
    private final Map<TabInfo, String> tabInfos = ContainerUtil.createConcurrentWeakMap();

    public TabbedPane(@NotNull DBNForm form) {
        jbTabs = JBTabsFactory.createTabs(form.ensureProject());
        Disposer.register(form, () -> Dispatch.run(this::disposeInner));
    }

    public void select(JComponent component, boolean requestFocus) {
        TabInfo tabInfo = jbTabs.findInfo(component);
        if (tabInfo != null) {
            jbTabs.select(tabInfo, requestFocus);
        }
    }

    @NotNull
    @Override
    public TabInfo addTab(TabInfo info, int index) {
        acknowledgeTab(info);
        return jbTabs.addTab(info, index);
    }

    @NotNull
    @Override
    public TabInfo addTab(TabInfo info) {
        acknowledgeTab(info);
        return jbTabs.addTab(info);
    }

    private void acknowledgeTab(TabInfo info) {
        checkDisposed();
        tabInfos.put(info, info.getText());
    }

    @NotNull
    @Override
    public ActionCallback removeTab(TabInfo tabInfo) {
        return removeTab(tabInfo, true);
    }

    @Override
    public void removeAllTabs() {
        jbTabs.removeAllTabs();
    }

    @Override
    public @NotNull ActionCallback select(@NotNull TabInfo info, boolean requestFocus) {
        return jbTabs.select(info, requestFocus);
    }

    @Override
    public @Nullable TabInfo getSelectedInfo() {
        return jbTabs.getSelectedInfo();
    }

    public ActionCallback removeTab(TabInfo info, boolean disposeComponent) {
        tabInfos.remove(info);
        Object object = info.getObject();
        ActionCallback actionCallback = jbTabs.removeTab(info);
        if (disposeComponent) {
            Disposer.dispose(object);
            info.setObject(null);
        }
        return actionCallback;
    }

    public void selectTab(String tabName) {
        if (tabName == null) return;

        for (TabInfo tabInfo : jbTabs.getTabs()) {
            if (Objects.equals(tabInfo.getText(), tabName)) {
                jbTabs.select(tabInfo, false);
                return;
            }
        }
    }

    public String getSelectedTabName() {
        TabInfo selectedInfo = jbTabs.getSelectedInfo();
        if (selectedInfo == null) return null;

        return selectedInfo.getText();
    }

    @Workaround //
    public void disposeInner() {
        if (disposed) return;
        disposed = true;

        for (TabInfo tabInfo : tabInfos.keySet()) {
            Object object = tabInfo.getObject();
            tabInfo.setObject(null);
            Disposer.dispose(object);
        }
        nullify();
    }

    @Override
    public int getTabCount() {
        return jbTabs.getTabCount();
    }

    @Override
    public @NotNull JBTabsPresentation getPresentation() {
        return jbTabs.getPresentation();
    }

    @Override
    public @Nullable DataProvider getDataProvider() {
        return jbTabs.getDataProvider();
    }

    @Override
    public JBTabs setDataProvider(@NotNull DataProvider dataProvider) {
        return jbTabs.setDataProvider(dataProvider);
    }

    @Override
    public @NotNull List<TabInfo> getTabs() {
        return jbTabs.getTabs();
    }

    @Override
    public @Nullable TabInfo getTargetInfo() {
        return jbTabs.getTargetInfo();
    }

    @Override
    public @NotNull JBTabs addTabMouseListener(@NotNull MouseListener listener) {
        return jbTabs.addTabMouseListener(listener);
    }

    @Override
    public JBTabs addListener(@NotNull TabsListener listener) {
        return jbTabs.addListener(listener);
    }

    @Override
    public JBTabs addListener(@NotNull TabsListener listener, @Nullable Disposable disposable) {
        return jbTabs.addListener(listener, disposable);
    }

    @Override
    public JBTabs setSelectionChangeHandler(SelectionChangeHandler handler) {
        return jbTabs.setSelectionChangeHandler(handler);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return jbTabs.getComponent();
    }

    @Override
    public @Nullable TabInfo findInfo(MouseEvent event) {
        return jbTabs.findInfo(event);
    }

    @Override
    public @Nullable TabInfo findInfo(Object object) {
        return jbTabs.findInfo(object);
    }

    @Override
    public @Nullable TabInfo findInfo(Component component) {
        return jbTabs.findInfo(component);
    }

    @Override
    public int getIndexOf(@NotNull TabInfo tabInfo) {
        return jbTabs.getIndexOf(tabInfo);
    }

    @Override
    public void requestFocus() {
        jbTabs.requestFocus();
    }

    @Override
    public void setNavigationActionBinding(String prevActionId, String nextActionId) {
        jbTabs.setNavigationActionBinding(prevActionId, nextActionId);
    }

    @Override
    public @NotNull JBTabs setPopupGroup(@NotNull ActionGroup popupGroup, @NotNull String place, boolean addNavigationGroup) {
        return jbTabs.setPopupGroup(popupGroup, place, addNavigationGroup);
    }

    @Override
    public @NotNull JBTabs setPopupGroup(@NotNull Supplier<? extends ActionGroup> popupGroup, @NotNull String place, boolean addNavigationGroup) {
        return jbTabs.setPopupGroup(popupGroup, place, addNavigationGroup);
    }

    @Override
    public void resetDropOver(TabInfo tabInfo) {
        jbTabs.resetDropOver(tabInfo);
    }

    @Override
    public Image startDropOver(TabInfo tabInfo, RelativePoint point) {
        return jbTabs.startDropOver(tabInfo, point);
    }

    @Override
    public void processDropOver(TabInfo over, RelativePoint point) {
        jbTabs.processDropOver(over, point);
    }

    @Override
    public @Nullable Component getTabLabel(TabInfo tabInfo) {
        return jbTabs.getTabLabel(tabInfo);
    }

    @Override
    public @NotNull TabInfo getTabAt(int i) {
        return jbTabs.getTabAt(i);
    }

    public void setTabsPosition(JBTabsPosition jbTabsPosition) {
        ((JBTabsPresentation) jbTabs).setTabsPosition(jbTabsPosition);
    }

    public void setHideTabs(boolean b) {
        ((JBTabsPresentation) jbTabs).setHideTabs(b);
    }
}
