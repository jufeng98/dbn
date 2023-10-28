package com.dci.intellij.dbn.common.ui.form;

import com.dci.intellij.dbn.common.action.DataProviders;
import com.dci.intellij.dbn.common.dispose.ComponentDisposer;
import com.dci.intellij.dbn.common.environment.options.EnvironmentSettings;
import com.dci.intellij.dbn.common.notification.NotificationSupport;
import com.dci.intellij.dbn.common.ui.component.DBNComponentBase;
import com.dci.intellij.dbn.common.ui.misc.DBNButton;
import com.dci.intellij.dbn.options.general.GeneralProjectSettings;
import com.intellij.ide.DataManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.HashSet;
import java.util.Set;

import static com.dci.intellij.dbn.common.ui.util.UserInterface.*;

public abstract class DBNFormBase
        extends DBNComponentBase
        implements DBNForm, NotificationSupport {

    private boolean initialized;
    private final Set<JComponent> enabled = new HashSet<>();

    public DBNFormBase(@Nullable Disposable parent) {
        super(parent);
    }

    public DBNFormBase(@Nullable Disposable parent, @Nullable Project project) {
        super(parent, project);
    }

    @NotNull
    @Override
    public final JComponent getComponent() {
        JComponent component = getMainComponent();
        if (!initialized) {
            initialize();
        }
        return component;
    }

    private void initialize() {
        initialized = true;
        JComponent mainComponent = getMainComponent();
        DataProviders.register(mainComponent, this);
        updateScrollPaneBorders(mainComponent);
        updateTitledBorders(mainComponent);
        updateSplitPanes(mainComponent);

        //GuiUtils.replaceJSplitPaneWithIDEASplitter(mainComponent);
    }

    protected abstract JComponent getMainComponent();

    public EnvironmentSettings getEnvironmentSettings(Project project) {
        return GeneralProjectSettings.getInstance(project).getEnvironmentSettings();
    }

    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        return null;
    }

    @Override
    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public void disposeInner() {
        JComponent component = getComponent();
        DataManager.removeDataProvider(component);
        ComponentDisposer.dispose(component);
        nullify();
    }

    public void freeze() {
        visitRecursively(getComponent(), c -> disable(c));
    }

    public void unfreeze() {
        visitRecursively(getComponent(), c -> enable(c));
    }

    private void disable(JComponent c) {
        if (c instanceof AbstractButton ||
                c instanceof JTextComponent ||
                c instanceof ActionToolbar ||
                c instanceof DBNButton) {

            if (c.isEnabled()) {
                enabled.add(c);
                c.setEnabled(false);
            }
        }
    }

    private void enable(JComponent c) {
        if (enabled.remove(c)) {
            c.setEnabled(true);
        }
    }
}
