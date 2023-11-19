package com.dbn.object.filter.name.action;

import com.dbn.object.filter.name.ObjectNameFilterManager;
import com.dbn.object.filter.name.ui.ObjectNameFilterSettingsForm;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.util.Strings.cachedUpperCase;

public class ObjectTypeFilterCreateAction extends AbstractObjectFilterAction {
    private final DBObjectType objectType;

    ObjectTypeFilterCreateAction(DBObjectType objectType, ObjectNameFilterSettingsForm settingsForm) {
        super(cachedUpperCase(objectType.getName()), objectType.getIcon(), settingsForm);
        this.objectType = objectType;
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ObjectNameFilterManager filterManager = ObjectNameFilterManager.getInstance(project);
        filterManager.createFilter(objectType, settingsForm);
    }
}
