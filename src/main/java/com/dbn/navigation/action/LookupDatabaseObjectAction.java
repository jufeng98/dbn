package com.dbn.navigation.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class LookupDatabaseObjectAction extends GoToDatabaseObjectAction{
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText("Lookup Database Object...");
    }
}
