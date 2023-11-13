package com.dbn.common.ui;

import com.dbn.common.latent.Latent;

import javax.swing.JComponent;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FontMetrics {
    private final JComponent component;
    private final Map<String, Map<Font, int[]>> cache = new HashMap<>();

    private final Latent<FontRenderContext> fontRenderContext = Latent.mutable(
            () -> getComponent().getFont(),
            () -> getComponent().getFontMetrics(getComponent().getFont()).getFontRenderContext());

    public FontMetrics(JComponent component) {
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }

    public int getTextWidth(String group, String text) {
        Map<Font, int[]> cache = this.cache.computeIfAbsent(group, k -> new HashMap<>());
        int length = text.length();
        if (length == 0) {
            return 0;
        }

        Font font = component.getFont();
        int len = Math.min(100, length);

        int[] widths = cache.compute(font, (f, v) -> v == null ? new int[len] : v.length < len ? Arrays.copyOf(v, len) : v);
        int index = len - 1;
        if (widths[index] == 0) {
            widths[index] = (int) font.getStringBounds(text, fontRenderContext.get()).getWidth();
        }

        return widths[index];
    }

}
