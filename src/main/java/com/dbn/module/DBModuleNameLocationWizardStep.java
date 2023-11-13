package com.dbn.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;

import javax.swing.*;

public class DBModuleNameLocationWizardStep extends DBModuleWizardStep {

    public DBModuleNameLocationWizardStep(WizardContext wizardContext, ModuleBuilder moduleBuilder, ModulesProvider modulesProvider) {
        super(wizardContext,  moduleBuilder, modulesProvider);
    }

    @Override
    public JComponent getComponent() {
        return new JPanel();
    }

    @Override
    public void updateDataModel() {

    }
}
