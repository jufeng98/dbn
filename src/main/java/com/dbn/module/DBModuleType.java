package com.dbn.module;

import com.dbn.common.icon.Icons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DBModuleType<T extends DBModuleBuilder> extends ModuleType<T> {
    public static final DBModuleType MODULE_TYPE = new DBModuleType("DBNavigator.ModuleType.DatabaseModule");
    protected DBModuleType(String id) {
        super(id);
    }

    @Override
    @NotNull
    public T createModuleBuilder() {
        return (T) new DBModuleBuilder();
    }

    @Override
    @NotNull
    public String getName() {
        return "Database module";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "<b>Database module</b> is a part of a project which bundles all database related objects like ddl/dml scripts, database connections and so on...";
    }

    public Icon getBigIcon() {
        return Icons.DATABASE_MODULE;
    }

    @NotNull
    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return isOpened ? Icons.DATABASE_MODULE_SMALL_OPEN : Icons.DATABASE_MODULE_SMALL_CLOSED;
    }
    @Override
    @NotNull
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull T moduleBuilder, @NotNull ModulesProvider modulesProvider) {
        List<ModuleWizardStep> wizardSteps = new ArrayList<>();

        //ProjectWizardStepFactory.getInstance().createNameAndLocationStep(wizardContext);
        return wizardSteps.toArray(new ModuleWizardStep[0]);
    }


}
