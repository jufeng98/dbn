package com.dbn.common.options;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.project.ProjectSupplier;
import com.intellij.openapi.Disposable;

public interface ProjectConfiguration<P extends ProjectConfiguration<?, ?>, E extends ConfigurationEditorForm<?>>
        extends Configuration<P, E>, ProjectSupplier, Disposable {

    @Override
    default void dispose() {
    }
}
