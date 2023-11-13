package com.dbn.connection.transaction;

import com.dbn.common.icon.Icons;
import com.dbn.common.option.InteractiveOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public enum TransactionOption implements InteractiveOption{
    ASK("Ask", null),
    COMMIT("Commit", Icons.CONNECTION_COMMIT),
    ROLLBACK("Rollback", Icons.CONNECTION_ROLLBACK),
    REVIEW_CHANGES("Review changes", null),
    CANCEL("Cancel", null);

    private String name;
    private Icon icon;

    TransactionOption(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }


    @Nullable
    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
