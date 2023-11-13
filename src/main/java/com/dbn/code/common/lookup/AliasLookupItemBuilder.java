package com.dbn.code.common.lookup;

import com.dbn.code.common.completion.CodeCompletionContext;

import javax.swing.*;

public class AliasLookupItemBuilder extends LookupItemBuilder {
    private final CharSequence text;
    private final boolean definition;

    public AliasLookupItemBuilder(CharSequence text, boolean definition) {
        this.text = text;
        this.definition = definition;
    }

    @Override
    public String getTextHint() {
        return definition ? "alias def" : "alias ref";
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