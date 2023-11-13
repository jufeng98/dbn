package com.dbn.code.common.style.formatting;

import com.dbn.code.common.style.presets.CodeStylePreset;
import com.intellij.formatting.Wrap;
import org.jdom.Element;

import static com.dbn.common.options.setting.Settings.enumAttribute;

public enum WrapDefinition implements FormattingAttribute<Wrap>{
    NONE    (new Loader(){
        @Override
        Wrap load(){return CodeStylePreset.WRAP_NONE;}}),
    NORMAL  (new Loader(){
        @Override
        Wrap load(){return CodeStylePreset.WRAP_NORMAL;}}),
    ALWAYS  (new Loader(){
        @Override
        Wrap load(){return CodeStylePreset.WRAP_ALWAYS;}}),
    IF_LONG (new Loader(){
        @Override
        Wrap load(){return CodeStylePreset.WRAP_IF_LONG;}});

    private Wrap value;
    private Loader<Wrap> loader;

    private WrapDefinition(Loader<Wrap> loader) {
        this.loader = loader;
    }

    @Override
    public Wrap getValue() {
        if (value == null && loader != null) {
            value = loader.load();
            loader = null;
        }
        return value;
    }

    public static WrapDefinition get(Element element) {
        return enumAttribute(element, "formatting-wrap", WrapDefinition.class);
    }
}
