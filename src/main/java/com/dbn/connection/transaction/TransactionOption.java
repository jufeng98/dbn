package com.dbn.connection.transaction;

import com.dbn.common.icon.Icons;
import com.dbn.common.option.InteractiveOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;

import static com.dbn.nls.NlsResources.nls;

@Getter
@AllArgsConstructor
public enum TransactionOption implements InteractiveOption{
    ASK(nls("cfg.connection.const.TransactionOption_ASK"), null),
    COMMIT(nls("cfg.connection.const.TransactionOption_COMMIT"), Icons.CONNECTION_COMMIT),
    ROLLBACK(nls("cfg.connection.const.TransactionOption_ROLLBACK"), Icons.CONNECTION_ROLLBACK),
    REVIEW_CHANGES(nls("cfg.connection.const.TransactionOption_REVIEW_CHANGES"), null),
    CANCEL(nls("cfg.connection.const.TransactionOption_CANCEL"), null);

    private final String name;
    private final Icon icon;

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }
}
