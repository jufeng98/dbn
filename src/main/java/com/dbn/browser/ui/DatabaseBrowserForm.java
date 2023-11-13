package com.dbn.browser.ui;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.common.ui.form.DBNForm;
import com.dbn.common.ui.form.DBNFormBase;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public abstract class DatabaseBrowserForm extends DBNFormBase {
    public DatabaseBrowserForm(DBNForm parent) {
        super(parent);
    }

    DatabaseBrowserForm(Project project) {
        super(null, project);
    }

    @Nullable
    public abstract DatabaseBrowserTree getBrowserTree();

    public abstract void selectElement(BrowserTreeNode treeNode, boolean focus, boolean scroll);

    public abstract void rebuildTree();
}
