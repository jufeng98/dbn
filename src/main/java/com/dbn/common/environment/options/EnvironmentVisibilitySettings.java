package com.dbn.common.environment.options;


import com.dbn.common.options.PersistentConfiguration;
import com.dbn.common.options.setting.BooleanSetting;
import lombok.Getter;
import org.jdom.Element;

@Getter
public class EnvironmentVisibilitySettings implements PersistentConfiguration {
    private final BooleanSetting connectionTabs = new BooleanSetting("connection-tabs", true);
    private final BooleanSetting objectEditorTabs = new BooleanSetting("object-editor-tabs", true);
    private final BooleanSetting scriptEditorTabs = new BooleanSetting("script-editor-tabs", false);
    private final BooleanSetting dialogHeaders = new BooleanSetting("dialog-headers", true);
    private final BooleanSetting executionResultTabs = new BooleanSetting("execution-result-tabs", true);

    @Override
    public void readConfiguration(Element element) {
        connectionTabs.readConfiguration(element);
        dialogHeaders.readConfiguration(element);
        objectEditorTabs.readConfiguration(element);
        scriptEditorTabs.readConfiguration(element);
        executionResultTabs.readConfiguration(element);
    }

    @Override
    public void writeConfiguration(Element element) {
        connectionTabs.writeConfiguration(element);
        dialogHeaders.writeConfiguration(element);
        objectEditorTabs.writeConfiguration(element);
        scriptEditorTabs.writeConfiguration(element);
        executionResultTabs.writeConfiguration(element);
    }
}
