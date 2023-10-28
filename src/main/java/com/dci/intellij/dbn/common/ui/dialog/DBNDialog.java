package com.dci.intellij.dbn.common.ui.dialog;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.project.ProjectRef;
import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.form.DBNForm;
import com.dci.intellij.dbn.common.ui.util.Listeners;
import com.dci.intellij.dbn.common.util.Commons;
import com.dci.intellij.dbn.common.util.Titles;
import com.dci.intellij.dbn.diagnostics.Diagnostics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.dci.intellij.dbn.common.ui.dialog.DBNDialogListener.Action.CLOSE;
import static com.dci.intellij.dbn.common.ui.dialog.DBNDialogListener.Action.OPEN;

public abstract class DBNDialog<F extends DBNForm> extends DialogWrapper implements DBNComponent {
    private F form;
    private final ProjectRef project;
    private boolean rememberSelection;
    private boolean disposed;
    private Dimension defaultSize;
    private final Listeners<DBNDialogListener> listeners = Listeners.create(getDisposable());

    protected DBNDialog(Project project, String title, boolean canBeParent) {
        super(project, canBeParent);
        this.project = ProjectRef.of(project);
        setTitle(Titles.signed(title));
        getHelpAction().setEnabled(false);
    }

    @Override
    protected void init() {
        if (defaultSize != null) {
            setSize(
                (int) defaultSize.getWidth(),
                (int) defaultSize.getHeight());
        }
        super.init();
    }

    public void addDialogListener(DBNDialogListener listener) {
        listeners.add(listener);
    }

    public void setDefaultSize(int width, int height) {
        this.defaultSize = new Dimension(width, height);
    }

    @NotNull
    public final F getForm() {
        if (form == null && !isDisposed()) {
            form = createForm();
        }
        return Failsafe.nn(form);
    }

    @Override
    public final void show() {
        super.show();
        listeners.notify(l -> l.onAction(OPEN));
    }

    @Override
    @NotNull
    protected final JComponent createCenterPanel() {
        return getComponent();
    }

    @NotNull
    protected abstract F createForm();

    @Nullable
    public final <T extends Disposable> T getParentComponent() {
        return null;
    }

    @NotNull
    @Override
    public final JComponent getComponent() {
        return getForm().getComponent();
    }

    @Override
    protected String getDimensionServiceKey() {
        return Diagnostics.isDialogSizingReset() ? null : "DBNavigator." + getClass().getSimpleName();
    }

    protected static void renameAction(@NotNull Action action, String name) {
        action.putValue(Action.NAME, name);
    }

    protected static void makeDefaultAction(@NotNull Action action) {
        action.putValue(DEFAULT_ACTION, Boolean.TRUE);
    }

    protected static void makeFocusAction(@NotNull Action action) {
        action.putValue(FOCUSED_ACTION, Boolean.TRUE);
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        if (isDisposed()) return null;

        return Commons.coalesce(
                () -> getForm().getPreferredFocusedComponent(),
                () -> super.getPreferredFocusedComponent(),
                () -> getButton(getOKAction()),
                () -> getButton(getCancelAction()));
    }

    @Override
    protected void doHelpAction() {
        super.doHelpAction();
    }

    @Override
    @NotNull
    public Project getProject() {
        return project.ensure();
    }

    public boolean isRememberSelection() {
        return rememberSelection;
    }

    public void registerRememberSelectionCheckBox(JCheckBox rememberSelectionCheckBox) {
        rememberSelectionCheckBox.addActionListener(e -> rememberSelection = rememberSelectionCheckBox.isSelected());
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public final void dispose() {
        if (disposed) return;
        disposed = true;

        listeners.notify(l -> l.onAction(CLOSE));
        super.dispose();
        Disposer.dispose(form);
        disposeInner();
        //nullify();
    }
}
