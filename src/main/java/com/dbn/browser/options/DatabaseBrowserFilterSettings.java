package com.dbn.browser.options;

import com.dbn.browser.options.ui.DatabaseBrowserFilterSettingsForm;
import com.dbn.common.options.CompositeProjectConfiguration;
import com.dbn.common.options.Configuration;
import com.dbn.object.filter.type.ObjectTypeFilterSettings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class DatabaseBrowserFilterSettings
        extends CompositeProjectConfiguration<DatabaseBrowserSettings, DatabaseBrowserFilterSettingsForm> {

    private final ObjectTypeFilterSettings objectTypeFilterSettings = new ObjectTypeFilterSettings(this, null);

    DatabaseBrowserFilterSettings(DatabaseBrowserSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public DatabaseBrowserFilterSettingsForm createConfigurationEditor() {
        return new DatabaseBrowserFilterSettingsForm(this);
    }

    @Override
    public String getConfigElementName() {
        return "filters";
    }

    @Override
    public String getDisplayName() {
        return "Database Browser";
    }

    @Override
    public String getHelpTopic() {
        return "browserSettings";
    }

    /*********************************************************
     *                     Configuration                     *
     *********************************************************/

    @Override
    protected Configuration[] createConfigurations() {
        return new Configuration[] {objectTypeFilterSettings};
    }
}
