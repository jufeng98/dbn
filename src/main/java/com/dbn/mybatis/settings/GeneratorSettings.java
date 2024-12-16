package com.dbn.mybatis.settings;

import com.dbn.common.options.BasicProjectConfiguration;
import com.dbn.mybatis.model.Config;
import com.dbn.mybatis.ui.GeneratorSettingsEditorForm;
import com.dbn.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class GeneratorSettings extends BasicProjectConfiguration<MyBatisSettings, GeneratorSettingsEditorForm> {
    private volatile Map<String, Config> initConfigMap;
    private volatile Map<String, Config> historyConfigMap;

    public GeneratorSettings(MyBatisSettings parent) {
        super(parent);
    }

    @NotNull
    @Override
    public GeneratorSettingsEditorForm createConfigurationEditor() {
        return new GeneratorSettingsEditorForm(this);
    }

    /****************************************************
     *                   Configuration                  *
     ****************************************************/
    @Override
    public String getConfigElementName() {
        return "generator-filters";
    }

    @SneakyThrows
    @Override
    public void readConfiguration(Element element) {
        Map<String, Config> initMap = readConfig("initConfigMap", element);
        if (initMap != null) {
            initConfigMap = initMap;
        }

        Map<String, Config> historyMap = readConfig("historyConfigMap", element);
        if (historyMap != null) {
            historyConfigMap = historyMap;
        }
    }

    @Override
    public void writeConfiguration(Element element) {
        writeConfig("initConfigMap", initConfigMap, element);

        writeConfig("historyConfigMap", historyConfigMap, element);
    }

    @SneakyThrows
    private void writeConfig(String name, Map<String, Config> map, Element parent) {
        String jsonStr = "";
        if (map != null) {
            jsonStr = JsonUtils.OBJECT_MAPPER.writeValueAsString(map);
        }

        Element child = new Element(name);
        child.setText(jsonStr);

        parent.addContent(child);
    }

    @SneakyThrows
    private @Nullable Map<String, Config> readConfig(String name, Element element) {
        Element child = element.getChild(name);
        if (child == null) {
            return null;
        }

        String jsonStr = child.getText();
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }

        return JsonUtils.OBJECT_MAPPER.readValue(jsonStr, new TypeReference<HashMap<String, Config>>() {
        });
    }
}
