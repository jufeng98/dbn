package com.dci.intellij.dbn.plugin;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.component.ApplicationComponentBase;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.file.FileTypeService;
import com.dci.intellij.dbn.common.util.Dialogs;
import com.dci.intellij.dbn.language.common.DBLanguageFileType;
import com.dci.intellij.dbn.language.psql.PSQLFileType;
import com.dci.intellij.dbn.language.sql.SQLFileType;
import com.dci.intellij.dbn.plugin.ui.PluginConflictResolutionDialog;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.component.Components.applicationService;
import static com.dci.intellij.dbn.common.options.setting.Settings.getBoolean;
import static com.dci.intellij.dbn.common.options.setting.Settings.setBoolean;
import static com.dci.intellij.dbn.plugin.DBPluginStatus.*;
import static com.dci.intellij.dbn.plugin.PluginConflictManager.COMPONENT_NAME;

@Getter
@Setter
@State(
    name = COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class PluginConflictManager extends ApplicationComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Application.PluginConflictManager";

    private boolean fileTypesClaimed;
    private boolean conflictPrompted;

    public PluginConflictManager() {
        super(COMPONENT_NAME);
    }

    public static PluginConflictManager getInstance() {
        return applicationService(PluginConflictManager.class);
    }

    public void assesPluginConflict(Project project) {
        if (conflictPrompted) return;

        PluginStatusManager statusManager = PluginStatusManager.getInstance();
        DBPluginStatus sqlPluginStatus = statusManager.getSqlPluginStatus();
        DBPluginStatus dbnPluginStatus = statusManager.getDbnPluginStatus();

        if (dbnPluginStatus == ACTIVE)  {
            if (sqlPluginStatus == ACTIVE) {
                // both plugins in use, show resolution dialog
                showConflictResolutionDialog();
            } else {
                // missing or passive SQL plugin - favor DBN
                claimFileAssociations(false);
            }
        } else if (dbnPluginStatus == PASSIVE) {
            if (sqlPluginStatus == ACTIVE) {
                // SQL plugin user - restore default file associations
                restoreFileAssociations();
                
            } else if (sqlPluginStatus == PASSIVE) {
                // both plugins inactive - do nothing yet

                // ... or show conflict resolution?
                // showConflictResolutionDialog();
                
            } else if (sqlPluginStatus == MISSING) {
                // missing SQL plugin - favor DBN
                claimFileAssociations(false);
            }
        }
    }

    private void showConflictResolutionDialog() {
        conflictPrompted = true;
        Dialogs.show(() -> new PluginConflictResolutionDialog());
    }

    public void applyConflictResolution(PluginConflictResolution resolution) {
        switch (resolution) {
            case DISABLE_PLUGIN: disablePlugin(); return;
            case CONTINUE_FEATURED: claimFileAssociations(true); return;
            case CONTINUE_LIMITED: restoreFileAssociations(); return;
            case DECIDE_LATER: conflictPrompted = false; return;
            default:
        }
    }

    private void claimFileAssociations(boolean force) {
        // do not claim again if already claimed once (even if no longer associated with DBN)
        if (fileTypesClaimed && !force) return;

        FileTypeService fileTypeService = FileTypeService.getInstance();
        try {
            fileTypeService.claimFileAssociations(SQLFileType.INSTANCE);
            fileTypeService.claimFileAssociations(PSQLFileType.INSTANCE);
        } finally {
            if (force) fileTypesClaimed = true;
        }
    }

    private boolean areFileTypesClaimed() {
        FileTypeService fileTypeService = FileTypeService.getInstance();
        FileType fileType = fileTypeService.getCurrentFileType("sql");
        return fileType instanceof DBLanguageFileType;
    }

    private void restoreFileAssociations() {
        FileTypeService fileTypeService = FileTypeService.getInstance();
        fileTypeService.restoreFileAssociations();
    }

    private void disablePlugin() {
        // prompt again if needed on reinstall
        conflictPrompted = false;

        String pluginId = DatabaseNavigator.DBN_PLUGIN_ID.getIdString();
        PluginManager.disablePlugin(pluginId);
        ApplicationManagerEx.getApplicationEx().restart(true);
    }

    /**************************************************************************
     *                       PersistentStateComponent                         *
     **************************************************************************/

    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        setBoolean(element, "plugin-conflict-prompted", conflictPrompted);
        setBoolean(element, "file-types-claimed", fileTypesClaimed);

        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        conflictPrompted = getBoolean(element, "plugin-conflict-prompted", conflictPrompted);
        fileTypesClaimed = getBoolean(element, "file-types-claimed", fileTypesClaimed);
    }


}
