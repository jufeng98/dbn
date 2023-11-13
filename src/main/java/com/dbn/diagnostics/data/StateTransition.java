package com.dbn.diagnostics.data;

import com.dbn.common.color.Colors;
import com.intellij.ui.SimpleTextAttributes;
import lombok.Getter;

import java.awt.Color;

@Getter
public enum StateTransition {
    UNCHANGED(Category.NEUTRAL),
    IMPROVED(Category.GOOD),
    FIXED(Category.GOOD),
    DEGRADED(Category.BAD),
    BROKEN(Category.BAD);

    private final Category category;

    StateTransition(Category category) {
        this.category = category;
    }

    @Getter
    public enum Category {
        NEUTRAL(SimpleTextAttributes.GRAY_ATTRIBUTES.getFgColor(), false),
        GOOD(Colors.SUCCESS_COLOR, true),
        BAD(Colors.FAILURE_COLOR, true);

        private final Color color;
        private final boolean bold;
        private final SimpleTextAttributes textAttributes;

        Category(Color color, boolean bold) {
            this.color = color;
            this.bold = bold;

            textAttributes = new SimpleTextAttributes(bold ?
                    SimpleTextAttributes.STYLE_BOLD :
                    SimpleTextAttributes.STYLE_PLAIN, color);
        }
    }
}
