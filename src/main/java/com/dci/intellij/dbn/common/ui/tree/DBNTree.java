package com.dci.intellij.dbn.common.ui.tree;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.util.Fonts;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import static com.dci.intellij.dbn.common.dispose.ComponentDisposer.removeListeners;

public class DBNTree extends Tree implements DBNComponent {
    public static final DefaultTreeCellRenderer DEFAULT_CELL_RENDERER = new DefaultTreeCellRenderer();

    private final WeakRef<DBNComponent> parent;

    public DBNTree(@NotNull DBNComponent parent) {
        this.parent = WeakRef.of(parent);
        setTransferHandler(DBNTreeTransferHandler.INSTANCE);

        Disposer.register(parent, this);
    }

    public DBNTree(@NotNull DBNComponent parent, TreeModel treeModel) {
        super(treeModel);
        this.parent = WeakRef.of(parent);
        setTransferHandler(DBNTreeTransferHandler.INSTANCE);
        setFont(Fonts.getLabelFont());

        Disposer.register(parent, this);
        Disposer.register(this, treeModel);
    }

    public DBNTree(@NotNull DBNComponent parent, TreeNode root) {
        super(root);
        this.parent = WeakRef.of(parent);
        setTransferHandler(DBNTreeTransferHandler.INSTANCE);

        Disposer.register(parent, this);
        Disposer.register(this, root);
    }

    @Override
    public void setModel(TreeModel treeModel) {
        treeModel = Disposer.replace(getModel(), treeModel);
        super.setModel(treeModel);

        Disposer.register(this, treeModel);
    }

    @Nullable
    public final Project getProject() {
        return parent.ensure().getProject();
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return this;
    }

    @NotNull
    @Override
    public <T extends Disposable> T getParentComponent() {
        return (T) parent.ensure();
    }

    /********************************************************
     *                    Disposable                        *
     ********************************************************/
    @Getter
    @Setter
    private boolean disposed;

    @Override
    public void disposeInner() {
        getUI().uninstallUI(this);
        setSelectionModel(null);
        clearToggledPaths();
        removeListeners(this);
        nullify();
    }
}
