package com.dbn.code.common.lookup;

import com.dbn.code.common.completion.CodeCompletionContext;

import javax.swing.*;

public class VariableLookupItemBuilder extends LookupItemBuilder {

    private final CharSequence text;
    private final boolean isDefinition;

    public VariableLookupItemBuilder(CharSequence text, boolean isDefinition) {
        this.text = text;
        this.isDefinition = isDefinition;
    }

    @Override
    public String getTextHint() {
        return isDefinition ? "variable def" : "variable ref";
    }

    @Override
    public boolean isBold() {
        return false;
    }

    @Override
    public CharSequence getText(CodeCompletionContext completionContext) {
        return text;
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}