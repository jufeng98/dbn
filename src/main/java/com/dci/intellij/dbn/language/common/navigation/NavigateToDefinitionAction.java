package com.dci.intellij.dbn.language.common.navigation;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NavigateToDefinitionAction extends NavigationAction{
    public NavigateToDefinitionAction(@Nullable DBObject parentObject, @NotNull BasePsiElement navigationElement, @NotNull DBObjectType objectType) {
        super("Go to " + objectType.getName() + " Definition", Icons.NAVIGATION_GO_TO_BODY, parentObject, navigationElement);
    }
}