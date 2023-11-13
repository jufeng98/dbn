package com.dbn.common.color;

import com.intellij.ui.ColorUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public enum ColorAdjustment {
    BRIGHTER,
    DARKER,

    SOFTER,
    STRONGER;



    public Color adjust(Color color, int tones) {
        switch (this) {
            case BRIGHTER: return hackBrightness(color, tones, 1.03F);
            case DARKER: return hackBrightness(color, tones, 1 / 1.03F);

            case SOFTER: return tuneSaturation(color, tones, 1 / 1.03F);
            case STRONGER: return tuneSaturation(color, tones, 1.03F);
        }

        return color;
    }

    /*****************************************************************
     *           Copied over from {@link ColorUtil}
     *****************************************************************/

    private static Color hackBrightness(@NotNull Color color, int tones, float factor) {
        return tuneHSBComponent(color, 2, tones, factor);
    }

    private static Color tuneSaturation(@NotNull Color color, int tones, float factor) {
        return tuneHSBComponent(color, 1, tones, factor);
    }

    @NotNull
    private static Color tuneHSBComponent(@NotNull Color color, int componentIndex, int howMuch, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float component = hsb[componentIndex];
        for (int i = 0; i < howMuch; i++) {
            component = Math.min(1, Math.max(factor * component, 0));
            if (component == 0 || component == 1) break;
        }
        hsb[componentIndex] = component;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
