package com.dbn.common.ui.misc;

import com.intellij.openapi.actionSystem.ex.CheckboxAction;

import javax.swing.*;

public abstract class DBNCheckboxAction extends CheckboxAction{
    protected DBNCheckboxAction() {
    }

    protected DBNCheckboxAction(String text) {
        super(text);
    }

    protected DBNCheckboxAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }
}
