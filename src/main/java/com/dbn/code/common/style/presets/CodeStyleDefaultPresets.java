package com.dbn.code.common.style.presets;

import com.dbn.code.common.style.presets.iteration.IterationChopDownIfNotSinglePreset;

public class CodeStyleDefaultPresets {
    public static CodeStylePreset[] PRESETS = new CodeStylePreset[] {
          new IterationChopDownIfNotSinglePreset()
    };

}
