package com.dbn.code.common.style.options;

import com.dbn.code.common.style.presets.CodeStylePreset;
import com.dbn.common.options.PersistentConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.dbn.common.options.setting.Settings.stringAttribute;

@Getter
@Setter
@EqualsAndHashCode
public class CodeStyleFormattingOption implements PersistentConfiguration {
    private final Map<String, CodeStylePreset> presets = new LinkedHashMap<>();
    private String name;
    private String displayName;
    private CodeStylePreset preset;

    public CodeStyleFormattingOption(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public void addPreset(CodeStylePreset preset) {
        presets.put(preset.getId(), preset);
    }

    public void addPreset(CodeStylePreset preset, boolean makeDefault) {
        presets.put(preset.getId(), preset);
        if (makeDefault) this.preset = preset;
    }

    public CodeStylePreset[] getPresets() {
        return presets.values().toArray(new CodeStylePreset[0]);
    }

    private CodeStylePreset getPreset(String id) {
        return presets.get(id);
    }

    /*********************************************************
     *                PersistentConfiguration                *
     *********************************************************/
    @Override
    public void readConfiguration(Element element) {
        name = stringAttribute(element, "name");
        String presetId = stringAttribute(element, "value");
        CodeStylePreset newPreset = getPreset(presetId);
        if (newPreset != null) preset = newPreset;
    }

    @Override
    public void writeConfiguration(Element element) {
        element.setAttribute("name", name);
        element.setAttribute("value", preset.getId());
    }
}