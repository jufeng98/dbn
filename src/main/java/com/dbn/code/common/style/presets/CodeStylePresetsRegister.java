package com.dbn.code.common.style.presets;

import java.util.HashMap;
import java.util.Map;

public class CodeStylePresetsRegister {
    private static final Map<String, CodeStylePreset> wrapPresets = new HashMap<>();

    public static void registerWrapPreset(CodeStylePreset codeStylePreset) {
        wrapPresets.put(codeStylePreset.getId(), codeStylePreset);
    }

    public static CodeStylePreset getWrapPreset(String id) {
        return wrapPresets.get(id);
    }
}
