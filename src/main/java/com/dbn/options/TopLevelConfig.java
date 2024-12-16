package com.dbn.options;

import com.dbn.common.options.BasicConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;

public interface TopLevelConfig<P extends BasicConfiguration<?, ?>, E extends ConfigurationEditorForm<?>> {
    ConfigId getConfigId();

    @NotNull
    Configuration<P, E> getOriginalSettings();
}
