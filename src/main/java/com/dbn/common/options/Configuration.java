package com.dbn.common.options;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface Configuration<P extends Configuration, E extends ConfigurationEditorForm>
        extends SearchableConfigurable, PersistentConfiguration {

    P getParent();

    String getConfigElementName();

    @NotNull
    E createConfigurationEditor();

    E getSettingsEditor();

    E ensureSettingsEditor();

    Project resolveProject();
}
