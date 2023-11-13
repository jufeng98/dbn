package com.dbn.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;


public abstract class DBModuleWizardStep extends ModuleWizardStep {
    protected static final Icon ICON = IconLoader.getIcon("/addmodulewizard.png");
    public DBModuleWizardStep(WizardContext wizardContext, ModuleBuilder moduleBuilder, ModulesProvider modulesProvider) {
        this.wizardContext = wizardContext;
        this.moduleBuilder = moduleBuilder;
        this.modulesProvider = modulesProvider;
    }

    protected WizardContext wizardContext;
    protected ModuleBuilder moduleBuilder;
    protected ModulesProvider modulesProvider;

    public DBModuleBuilder getModuleBuilder() {
        return (DBModuleBuilder) moduleBuilder;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }



}
