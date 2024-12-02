package com.dbn.code.common.style.formatting;

import com.dbn.code.common.style.presets.CodeStylePreset;
import com.intellij.formatting.Spacing;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.enumAttribute;

public enum SpacingDefinition implements FormattingAttribute<Spacing>{
    NO_SPACE  (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_NO_SPACE;}}),
    ONE_SPACE (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_ONE_SPACE;}}),

    LINE_BREAK (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_LINE_BREAK;}}),
    ONE_LINE  (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_ONE_LINE;}}),

    MIN_LINE_BREAK (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_MIN_LINE_BREAK;}}),
    MIN_ONE_LINE  (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_MIN_ONE_LINE;}}),
    MIN_ONE_SPACE  (new Loader<>(){
        @Override
        Spacing load(){return CodeStylePreset.SPACING_MIN_ONE_SPACE;}}),
    ;

    private Spacing value;
    private Loader<Spacing> loader;

    SpacingDefinition(Loader<Spacing> loader) {
        this.loader = loader;
    }

    @Override
    public Spacing getValue() {
        if (value == null && loader != null) {
            value = loader.load();
            loader = null;
        }
        return value;
    }

    public static SpacingDefinition get(Element element, boolean before) {
        return before ?
                enumAttribute(element, "formatting-spacing-before", SpacingDefinition.class) :
                enumAttribute(element, "formatting-spacing-after", SpacingDefinition.class);
    }
}
