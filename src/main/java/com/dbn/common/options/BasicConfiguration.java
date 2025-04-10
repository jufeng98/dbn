package com.dbn.common.options;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.common.ref.WeakRef;
import com.dbn.options.TopLevelConfig;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Checks.isValid;

public abstract class BasicConfiguration<P extends Configuration<?, ?>, E extends ConfigurationEditorForm<?>>
        extends AbstractConfiguration<P, E> {

    private final transient boolean transitory = ConfigurationHandle.isTransitory();
    private final transient WeakRef<P> parent;
    private transient WeakRef<E> editorForm;
    private transient boolean modified = false;

    public BasicConfiguration(P parent) {
        this.parent = WeakRef.of(parent);
    }

    public P getParent() {
        return WeakRef.get(this.parent);
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }

    @Override
    @NotNull
    public String getId() {
        return getClass().getName();
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Nullable
    public final E getSettingsEditor() {
        return WeakRef.get(editorForm);
    }

    @NotNull
    public final E ensureSettingsEditor() {
        return Failsafe.nd(getSettingsEditor());
    }


    @Override
    @NotNull
    public JComponent createComponent() {
        E editorForm = createConfigurationEditor();
        this.editorForm = WeakRef.of(editorForm);
        return editorForm.getComponent();
    }

    public void setModified(boolean modified) {
        if (!isResetting()) {
            this.modified = modified;
        }
    }

    private static Boolean isResetting() {
        return ConfigurationHandle.isResetting();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    public final boolean isTransitory() {
        return transitory;
    }

    @Override
    public void apply() throws ConfigurationException {
        E editorForm = getSettingsEditor();
        if (isValid(editorForm)) {
            editorForm.applyFormChanges();
        }
        modified = false;

        if (this instanceof TopLevelConfig<?, ?> topLevelConfig) {
            Configuration<?, ?> originalSettings = topLevelConfig.getOriginalSettings();
            if (originalSettings != this) {
                Element settingsElement = new Element("settings");
                writeConfiguration(settingsElement);
                originalSettings.readConfiguration(settingsElement);
            }

            // Notify only when all changes are set
            ConfigurationHandle.notifyChanges();
        }
    }

    @Override
    public void reset() {
        try {
            ConfigurationHandle.setResetting(true);
            E editorForm = getSettingsEditor();
            if (editorForm != null) {
                editorForm.resetFormChanges();
            }
        } finally {
            modified = false;
            ConfigurationHandle.setResetting(false);
        }
    }

    @Override
    public void disposeUIResources() {
        editorForm = Disposer.replace(editorForm, null);
    }

    @NonNls
    public String getConfigElementName() {
        //throw new UnsupportedOperationException("Element name not defined for this configuration type.");
        return null;
    }

    protected static String nvl(String value) {
        return value == null ? "" : value;
    }


}
