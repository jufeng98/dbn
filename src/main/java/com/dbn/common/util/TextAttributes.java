package com.dbn.common.util;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.ui.SimpleTextAttributes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TextAttributes {

    public static SimpleTextAttributes getSimpleTextAttributes(TextAttributesKey textAttributesKey) {
        EditorColorsManager colorManager = EditorColorsManager.getInstance();
        com.intellij.openapi.editor.markup.TextAttributes textAttributes = colorManager.getGlobalScheme().getAttributes(textAttributesKey);
        if (textAttributes == null) {
            textAttributes = HighlighterColors.TEXT.getDefaultAttributes();
        }
        return new SimpleTextAttributes(
                textAttributes.getBackgroundColor(),
                textAttributes.getForegroundColor(),
                textAttributes.getEffectColor(),
                textAttributes.getFontType());
    }
}
