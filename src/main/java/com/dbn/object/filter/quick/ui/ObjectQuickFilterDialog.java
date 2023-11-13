package com.dbn.object.filter.quick.ui;

import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.object.common.list.DBObjectList;
import com.dbn.object.filter.quick.ObjectQuickFilter;
import com.dbn.object.filter.quick.ObjectQuickFilterManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

public class ObjectQuickFilterDialog extends DBNDialog<ObjectQuickFilterForm> {
    private final DBObjectList<?> objectList;
    public ObjectQuickFilterDialog(Project project, DBObjectList<?> objectList) {
        super(project, "Quick filter", true);
        this.objectList = objectList;
        setModal(true);
        //setResizable(false);
        renameAction(getOKAction(), "Apply");
        init();
    }

    @NotNull
    @Override
    protected ObjectQuickFilterForm createForm() {
        return new ObjectQuickFilterForm(this, objectList);
    }

    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{
                getOKAction(),
                new AbstractAction("Clear Filters") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getForm().getFilter().clear();
                        doOKAction();
                    }
                },
                getCancelAction()
        };
    }

    @Override
    public void doOKAction() {
        try {
            ObjectQuickFilterManager quickFilterManager = ObjectQuickFilterManager.getInstance(getProject());
            ObjectQuickFilter<?> filter = getForm().getFilter();
            quickFilterManager.applyFilter(getForm().getObjectList(), filter.isEmpty() ? null : filter);
        } finally {
            super.doOKAction();
        }
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }
}
