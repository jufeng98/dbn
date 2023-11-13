package com.dci.intellij.dbn.language.common.navigation;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class NavigateToSpecificationAction extends NavigationAction{
    public NavigateToSpecificationAction(DBObject parentObject, @NotNull BasePsiElement navigationElement, @NotNull DBObjectType objectType) {
        super("Go to " + objectType.getName() + " Specification", Icons.NAVIGATION_GO_TO_SPEC, parentObject, navigationElement);
    }
}
