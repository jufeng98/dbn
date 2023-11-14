package com.dbn.options.action;

import com.dbn.options.ConfigId;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProjectSettingsActionGroup extends ActionGroup {
    private ConfigId[] configIds;
    public ProjectSettingsActionGroup(ConfigId ... configIds) {
        super("Settings", true);
        this.configIds = configIds;
    }

    @NotNull
    @Override
    public AnAction[] getChildren(AnActionEvent e) {
        List<AnAction> actions = new ArrayList<>();
        for (ConfigId configId : configIds) {
            actions.add(new ProjectSettingsAction(configId));
        }
        return actions.toArray(new AnAction[0]);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        //e.getPresentation().setIcon(Icons.ACTION_SETTINGS);
    }

}
