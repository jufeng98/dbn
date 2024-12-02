package com.dbn.code.common.style.formatting;

import com.dbn.common.util.Commons;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import lombok.Getter;

@Getter
public class FormattingAttributes {
    public static final FormattingAttributes NO_ATTRIBUTES = new FormattingAttributes(null, null, null, null);

    @Getter
    public enum Type {
        WRAP(true),
        INDENT(true),
        SPACING_BEFORE(true),
        SPACING_AFTER(false);

        final boolean left;
        Type(boolean left) {
            this.left = left;
        }
    }



    private Wrap wrap;
    private Indent indent;
    private Spacing spacingBefore;
    private Spacing spacingAfter;

    public FormattingAttributes(Wrap wrap, Indent indent, Spacing spacingBefore, Spacing spacingAfter) {
        this.wrap = wrap;
        this.indent = indent;
        this.spacingBefore = spacingBefore;
        this.spacingAfter = spacingAfter;
    }

    public FormattingAttributes(FormattingAttributes source) {
        this.wrap = source.wrap;
        this.indent = source.indent;
        this.spacingBefore = source.spacingBefore;
        this.spacingAfter = source.spacingAfter;
    }

    public static FormattingAttributes copy(FormattingAttributes source) {
        return source == null ? null : new FormattingAttributes(source);
    }

    public static FormattingAttributes merge(FormattingAttributes attributes, FormattingAttributes defaultAttributes) {
        if (defaultAttributes != null) {
            if (attributes == null) {
                attributes = new FormattingAttributes(defaultAttributes);
            } else {
                attributes.wrap = Commons.nvln(attributes.wrap, defaultAttributes.wrap);
                attributes.indent = Commons.nvln(attributes.indent, defaultAttributes.indent);
                attributes.spacingBefore = Commons.nvln(attributes.spacingBefore, defaultAttributes.spacingBefore);
                attributes.spacingAfter = Commons.nvln(attributes.spacingAfter, defaultAttributes.spacingAfter);
            }
        }
        return attributes;
    }

    @SuppressWarnings("unused")
    public static FormattingAttributes overwrite(FormattingAttributes attributes, FormattingAttributes defaultAttributes) {
        if (attributes != null && defaultAttributes != null) {
            attributes.wrap = Commons.nvln(defaultAttributes.wrap, attributes.wrap);
            attributes.indent = Commons.nvln(defaultAttributes.indent, attributes.indent);
            attributes.spacingBefore = Commons.nvln(defaultAttributes.spacingBefore, attributes.spacingBefore);
            attributes.spacingAfter = Commons.nvln(defaultAttributes.spacingAfter, attributes.spacingAfter);
        }
        return attributes;
    }

    public boolean isEmpty() {
        return wrap == null && indent == null && spacingBefore == null && spacingAfter == null;
    }

    public Object getAttribute(Type type) {
        return switch (type) {
            case WRAP -> wrap;
            case INDENT -> indent;
            case SPACING_BEFORE -> spacingBefore;
            case SPACING_AFTER -> spacingAfter;
        };
    }

    public static Object getAttribute(FormattingAttributes attributes, Type type) {
        return attributes == null ? null : attributes.getAttribute(type);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (wrap != null) result.append(" wrap=").append(wrap);
        if (indent != null) result.append(" indent=").append(indent);
        if (spacingBefore != null) result.append(" spacingBefore=").append(spacingBefore);
        if (spacingAfter != null) result.append(" spacingAfter=").append(spacingAfter);

        return result.toString();
    }
}
