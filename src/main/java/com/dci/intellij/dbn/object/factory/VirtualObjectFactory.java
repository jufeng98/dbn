package com.dci.intellij.dbn.object.factory;

import com.dci.intellij.dbn.common.component.Components;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.ref.WeakRefCache;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.object.common.DBVirtualObject;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class VirtualObjectFactory extends ProjectComponentBase {

    public static final String COMPONENT_NAME = "DBNavigator.Project.DatabaseObjectFactory";

    private final WeakRefCache<DBVirtualObject, Boolean> cache = WeakRefCache.weakKey();

    private VirtualObjectFactory(Project project) {
        super(project, COMPONENT_NAME);
    }

    public static VirtualObjectFactory getInstance(@NotNull Project project) {
        return Components.projectService(project, VirtualObjectFactory.class);
    }


    public DBVirtualObject createVirtualObject(BasePsiElement<?> psiElement) {
        DBVirtualObject virtualObject = new DBVirtualObject(psiElement);
        cache.set(virtualObject, true);
        return virtualObject;
    }

    @Override
    public void disposeInner() {
        Disposer.dispose(cache.keys());
        super.disposeInner();
    }
}