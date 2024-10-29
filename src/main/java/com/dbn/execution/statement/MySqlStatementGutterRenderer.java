package com.dbn.execution.statement;

import com.dbn.common.compatibility.Workaround;
import com.dbn.common.util.Traces;
import com.dbn.execution.statement.action.MySqlStatementGutterAction;
import com.dbn.sql.psi.SqlRoot;
import com.intellij.codeInsight.daemon.impl.ShowIntentionsPass;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author yudong
 */
public class MySqlStatementGutterRenderer extends GutterIconRenderer {
    private final MySqlStatementGutterAction action;
    private final int hashCode;

    public MySqlStatementGutterRenderer(SqlRoot sqlRoot) {
        this.action = new MySqlStatementGutterAction(sqlRoot);
        hashCode = Objects.hashCode(sqlRoot);
    }

    @Override
    @NotNull
    public Icon getIcon() {
        return action.getIcon();
    }

    @Override
    public boolean isNavigateAction() {
        return true;
    }

    @Override
    @Nullable
    @Workaround
    public AnAction getClickAction() {
        return Traces.isCalledThrough(ShowIntentionsPass.class) ? null : action;
    }

    @Override
    @Nullable
    public String getTooltipText() {
        return action.getTooltipText();
    }

    @NotNull
    @Override
    public Alignment getAlignment() {
        return Alignment.RIGHT;
    }

    @Override
    public boolean equals(Object o) {
        // prevent double gutter actions
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MySqlStatementGutterRenderer that = (MySqlStatementGutterRenderer) o;
        return Objects.equals(this.action.getPsiElement(), that.action.getPsiElement());
    }

    @Override
    public int hashCode() {
        // prevent double gutter actions
        return hashCode;
    }
}
