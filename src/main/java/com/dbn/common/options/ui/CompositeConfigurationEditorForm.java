package com.dbn.common.options.ui;

import com.dbn.common.options.CompositeConfiguration;
import com.intellij.openapi.options.ConfigurationException;

public abstract class CompositeConfigurationEditorForm<E extends CompositeConfiguration> extends ConfigurationEditorForm<E> {
    protected CompositeConfigurationEditorForm(E configuration) {
        super(configuration);
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
    }

    @Override
    public void resetFormChanges() {
    }
}
