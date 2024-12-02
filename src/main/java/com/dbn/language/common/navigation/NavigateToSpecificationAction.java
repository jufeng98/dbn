package com.dbn.language.common.navigation;

import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.common.icon.Icons;
import com.dbn.object.common.DBObject;
import com.dbn.object.type.DBObjectType;
import org.jetbrains.annotations.NotNull;

public class NavigateToSpecificationAction extends NavigationAction{
    public NavigateToSpecificationAction(DBObject parentObject,
                                         @NotNull BasePsiElement<?> navigationElement,
                                         @NotNull DBObjectType objectType) {
        super("Go to " + objectType.getName() + " Specification", Icons.NAVIGATION_GO_TO_SPEC,
                parentObject, navigationElement);
    }
}
