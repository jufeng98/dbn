package com.dbn.code.common.lookup;

import com.dbn.code.common.completion.CodeCompletionContext;

import javax.swing.*;

public class BasicLookupItemBuilder extends LookupItemBuilder {
    private CharSequence text;
    String hint;
    Icon icon;

    public BasicLookupItemBuilder(CharSequence text, String hint, Icon icon) {
        this.text = text;
        this.hint = hint;
        this.icon = icon;
    }

    @Override
    public String getTextHint() {
        return hint;
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
        return icon;
    }
}